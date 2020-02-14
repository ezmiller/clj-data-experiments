(ns airbnb-berlin
  (:require [libpython-clj.python :as py]))

(py/initialize! :library-path "/Users/emiller/.pyenv/shims/python3.6m")

;; (require-python '([nltk :as nltk]))

(-> (pt/read-csv "mycsv.csv")
    (pt/subset-cols "Col1" "Col2" "Col3")
    pt/median)



