(ns main
  (:require [tablecloth.api :as tc]
            [tablecloth.column.api :as tcc]
            [tech.v3.datatype :as dtype]
            [tech.v3.datatype.functional :as fun]
            [scicloj.clay.v1.api :as clay]
            [scicloj.clay.v1.tools :as tools]
            [scicloj.clay.v1.extensions :as ext]
            [scicloj.viz.api :as viz]))

(comment
  (clay/restart! {:tools [#_tools/scittle
                          tools/clerk]
                  :extensions [ext/dataset]})
  ,)

;;; Quick Look at Tablecloth

(str "Hello" "World!")

(def ds (tc/dataset "./data/flights.csv" {:key-fn keyword}))

(tc/head ds)

(tc/info ds)

(-> ds
    (tc/group-by [:hour])
    (tc/aggregate {:num-flights tc/row-count})
    (viz/data)
    (viz/x :hour)
    (viz/y :num-flights)
    (viz/type :bar)
    (viz/viz)
    )


(-> {:x (range 1000000) 
     :y (range 1000000)}
    (tc/dataset)
    (tc/map-columns :z
                    [:x :y]
                    (fn [& rows] (reduce + rows))))

;; Observation: right now tablecloth functions take datasets.
;; Often, that is just fine...

(-> ds
    ;; a question that we may ask though...can this be nicer?
    (tc/add-column :night-flight?
                   (fn [ds]
                     (dtype/emap #(and (> % 0) (< 6 %))
                                 :boolean
                                 (ds :hour))))
    (tc/select-columns [:origin :dest :night-flight?])
    (tc/group-by [:dest]) 
    (tc/select-rows (comp true? :night-flight?))
    (tc/aggregate {:night-flights tc/row-count})
    (tc/order-by [:night-flights] :desc))


;; but what if we want to work with a column...?
(def air-time (:air_time ds))

air-time

;; hmm what type is it?
(-> air-time
    first
    type
    )

;; but this is not quite right, or we are out of the world of
;; types within the tech system.
(-> air-time
    dtype/elemwise-datatype)

;; But this is the concrete type, that's arguably an internal
;; detail of the library under tablecloth, tech.ml.dataset,
;; which is closer to the metal.

;; So now we get into what I'm working on in this project...

;; In tablecloth, we might not need to know about the concrete type.
;; We want to know something more general. So we have a notion of
;; concrete and general types.
(tablecloth.api.utils/->general-types :int16)

(tablecloth.column.api/typeof air-time)

(tablecloth.column.api/typeof? air-time :textual)


(-> air-time
    (dtype/elemwise-cast :string)
    #_(->>
     (dtype/emap
      (fn [val] (if (count val)
                      (str (subs val 0 1) ":" (subs val 2 3))
                      (str (subs val 0 0) ":" (subs val 1 2))))
      :string))
    )

(tc/info air-time)
