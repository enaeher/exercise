(ns exercise.record-test
  (:require
   [clojure.test :refer [deftest is]]
   [exercise.record :as subject]
   [java-time :as time]
   [matcher-combinators.test]))

(def sample-input
  ["Smith | Jane | jane@example.com | blue | 1/2/2000"
   "Jones, John, john@example.com, red, 4/5/1990"
   "Public John john@example.com green 5/6/1985"])

(def records
  [{:last-name "Smith"
    :first-name "Jane"
    :email "jane@example.com"
    :favorite-color "blue"
    :date-of-birth (time/local-date 2000 01 02)}
   {:last-name "Jones"
    :first-name "John"
    :email "john@example.com"
    :favorite-color "red"
    :date-of-birth (time/local-date 1990 04 05)}
   {:last-name "Public"
    :first-name "John"
    :email "john@example.com"
    :favorite-color "green"
    :date-of-birth (time/local-date 1985 05 06)}])

(deftest from-string
  (is (= records (map subject/from-string sample-input))))

(deftest sort-1
  ;; sort by email (descending), then last name (ascending)
  (is (match? [{:email "john@example.com" :last-name "Jones"}
               {:email "john@example.com" :last-name "Public"}
               {:email "jane@example.com" :last-name "Smith"}]
              (subject/sort-1 records))))

(deftest sort-2
  ;; sort by birth date (ascending)
  (is (match? [{:date-of-birth (time/local-date 1985 05 06)}
               {:date-of-birth (time/local-date 1990 04 05)}
               {:date-of-birth (time/local-date 2000 01 02)}]
              (subject/sort-2 records))))

(deftest sort-3
  ;; sort by last name (descending)
  (is (match? [{:last-name "Smith"}
               {:last-name "Public"}
               {:last-name "Jones"}]
              (subject/sort-3 records))))

(deftest to-string
  (is (= ["Smith, Jane, jane@example.com, blue, 1/2/2000"
          "Jones, John, john@example.com, red, 4/5/1990"
          "Public, John, john@example.com, green, 5/6/1985"]
         (map subject/to-string records))))
