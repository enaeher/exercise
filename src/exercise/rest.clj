(ns exercise.rest
  (:require
   [cheshire.generate :refer [add-encoder]]
   [exercise.record :as record]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.json :as json]
   [ring.util.request :as request]
   [ring.util.response :as response]))

;; Correctly handle java.time.LocalDates when serializing JSON

(add-encoder
 java.time.LocalDate
 (fn [d generator]
   (.writeString generator (record/serialize-date d))))

;; Record storage

(defprotocol RecordStore
  (store-record! [self r]
    "Stores a record r for later retrieval. Returns the stored value.")
  (get-records [self]
    "Returns a vector of all previously-stored records."))

(defrecord InMemoryRecordStore [storage]
  RecordStore
  (store-record! [_ r]
    (swap! storage conj r))
  (get-records [_]
    @storage))

(defn make-in-memory-record-store []
  (->InMemoryRecordStore (atom [])))

;; Request handlers

(defn post-record [record-store request]
  (let [record (-> request
                   request/body-string
                   record/from-string)]
    (store-record! record-store record)
    (response/response record)))

(defn get-sorted-records [record-store sort-key]
  (->> (get-records record-store)
       (sort-by sort-key)
       response/response))

;; Routing and server

(defn handle-request [record-store {:keys [request-method uri] :as request}]
  (case [request-method uri]
    [:post "/records"]          (post-record record-store request)
    [:get "/records/email"]     (get-sorted-records record-store :email)
    [:get "/records/birthdate"] (get-sorted-records record-store :date-of-birth)
    [:get "/records/name"]      (get-sorted-records record-store (juxt :last-name :first-name))
    (response/response {:status 404 :body "Not Found"})))

(defn make-handler
  ([]
   (make-handler (make-in-memory-record-store)))
  ([record-store]
   (-> (partial handle-request record-store)
       json/wrap-json-response)))

(defn run [{:syms [--port] :as _opts
            :or {--port 3000}}]
  (jetty/run-jetty (make-handler) {:port --port}))
