(ns exercise.record
  (:require
   [clojure.string :as str]
   [java-time :as time]))

(def ^:private field-names
  [:last-name
   :first-name
   :email
   :favorite-color
   :date-of-birth])

(def ^:private get-field-names (apply juxt field-names))

(defn deserialize-date [s]
  (time/local-date "M/d/yyyy" s))

(defn serialize-date [date]
  (time/format "M/d/yyyy" date))

(defn from-string
  "Given a pipe-, comma-, or space-delimited string s such as:

  LastName | FirstName | Email | FavoriteColor | DateOfBirth
  LastName, FirstName, Email, FavoriteColor, DateOfBirth
  LastName FirstName Email FavoriteColor DateOfBirth

  Returns a map representing the parsed record. DateOfBirth is read as
  a Java LocalDate."
  [s]
  (let [raw-fields (->> (str/split s #"[\|\s\,]+")
                        (zipmap field-names))]
    (update raw-fields :date-of-birth deserialize-date)))

(defn sort-1
  "Sort by email (descending), then last name (ascending)"
  [rs]
  (sort
   (fn [x y]
     (compare [(:email y) (:last-name x)]
              [(:email x) (:last-name y)]))
   rs))

(defn sort-2
  "Sort by birth date (ascending)"
  [rs]
  (sort-by :date-of-birth rs))

(defn sort-3
  "Sort by last name (descending)"
  [rs]
  (sort-by
   :last-name
   (fn [x y] (compare y x))
   rs))

(defn to-string
  "Given a map representing a parsed record, returns a string in this format:

  LastName,FirstName,Email,FavoriteColor,DateOfBirth"
  [r]
  (->> (update r :date-of-birth serialize-date)
       get-field-names
       (str/join ", ")))
