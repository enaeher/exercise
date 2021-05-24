(ns exercise.cli
  (:require
   [exercise.record :as record]))

(defn- get-sort-fn [output]
  (case output
    1 record/sort-1
    2 record/sort-2
    3 record/sort-3
    record/sort-1))

(defn run [{:syms [--output] :as _opts}]
  (let [sort-fn (get-sort-fn --output)]
    (->> (line-seq (java.io.BufferedReader. *in*))
         (map record/from-string)
         (sort-fn)
         (map record/to-string)
         (run! println))))
