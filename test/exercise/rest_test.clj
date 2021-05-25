(ns exercise.rest-test
  (:require
   [cheshire.core :as json]
   [clojure.test :refer [deftest is testing]]
   [exercise.rest :as subject]
   [ring.mock.request :as mock]))

(def sample-input
  ["Aardvark, Zebulon, zebulon@example.com, red, 1/1/2001"
   "Badger, Yves, yves@example.com, green, 1/1/2002"
   "Coelecanth, Xerxes, xerxes@example.com, blue, 1/1/2003"])

(def expected-json-records
  [{:last-name "Aardvark"
    :first-name "Zebulon"
    :email "zebulon@example.com"
    :favorite-color "red"
    :date-of-birth "1/1/2001"}
   {:last-name "Badger"
    :first-name "Yves"
    :email "yves@example.com"
    :favorite-color "green"
    :date-of-birth "1/1/2002"}
   {:last-name "Coelecanth"
    :first-name "Xerxes"
    :email "xerxes@example.com"
    :favorite-color "blue"
    :date-of-birth "1/1/2003"}])

(defn- req [handler request]
  (-> (handler request)
      (update :body #(json/decode % true))))

(defn- response-ok? [response]
  (= 200 (:status response)))

(deftest post-records-then-get
  (let [handler (subject/make-handler)
        post-responses (mapv #(req handler (-> (mock/request :post "/records")
                                               (mock/body %)))
                             sample-input)]
    (is (every? response-ok? post-responses))

    (testing "get records sorted by email"
      (let [response (req handler (mock/request :get "/records/email"))]
        (is (response-ok? response))
        (is (= (sort-by :email expected-json-records)
               (:body response)))))

    (testing "get records sorted by birthdate"
      (let [response (req handler (mock/request :get "/records/birthdate"))]
        (is (response-ok? response))
        (is (= (sort-by :date-of-birth expected-json-records)
               (:body response)))))

    (testing "get records sorted by name"
      (let [response (req handler (mock/request :get "/records/name"))]
        (is (response-ok? response))
        (is (= (sort-by (juxt :last-name :first-name) expected-json-records)
               (:body response)))))))
