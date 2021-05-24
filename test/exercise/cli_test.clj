(ns exercise.cli-test
  (:require
   [clojure.test :refer [deftest is]]
   [exercise.cli :as subject]))

(def sample-input
  "Smith, Jane, jane@example.com, blue, 1/2/2000
Jones, John, john@example.com, red, 4/5/1990
Public, John, john@example.com, green, 5/6/1985")

(def expected-output-sort-1
  "Jones, John, john@example.com, red, 4/5/1990
Public, John, john@example.com, green, 5/6/1985
Smith, Jane, jane@example.com, blue, 1/2/2000
")

(def expected-output-sort-2
  "Public, John, john@example.com, green, 5/6/1985
Jones, John, john@example.com, red, 4/5/1990
Smith, Jane, jane@example.com, blue, 1/2/2000
")

(deftest run-default-sort
  (with-in-str sample-input
    (is (= expected-output-sort-1 (with-out-str (subject/run {}))))))

(deftest run-specify-sort
  (with-in-str sample-input
    (is (= expected-output-sort-2 (with-out-str (subject/run '{--output 2}))))))
