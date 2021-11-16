(ns fiddle)

(comment
  ;; starting the repl
  ;; clojure -Sdeps "$(curl -sL https://bit.ly/johnsdeps)" -M:ad-hoc/data-science:alpha/hot-load:repl/cider-refactor -r
  )

(require '[clojure.tools.deps.alpha.repl :refer [add-libs]])

;; (add-libs '{scicloj/notespace {:mvn/version "4-alpha-10"}})

(require '[scicloj.notespace.v4.api :as notespace]
         '[scicloj.kindly.kind :as kind])

(comment 
  (notespace/update-config
   #(assoc-in %
              [:by-ns *ns* :source-base-path]
              "/home/sakalli/projects/re_clojure2021/")))

(comment
  (notespace/restart!)

  (notespace/restart-events!)

  (scicloj.notespace.v4.frontend.engine/stop!)

  )

;;;;
(add-libs '{scicloj/tablecloth {:mvn/version "6.025"}})


(require '[tablecloth.api :as table])

(require '[tech.v3.datatype.functional :as fun])


(def raw-data (read-string (slurp "../zulip-scicloj.txt")))

(-> raw-data first keys)

(take 1 raw-data)

(def data (->> raw-data
               (map #(select-keys % [:id :stream_id :subject :sender_id :sender_full_name :type :timestamp :content]))
               (#(table/dataset % #_{:parser-fn {:timestamp [:local-date-time
                                                        (fn [el] (dtdt/milliseconds->datetime :local-date-time (* 1000 el)))]}}))))

^kind/dataset
(table/head data)

^kind/dataset
(table/info data)

^kind/dataset
(-> data
    (table/select-rows (comp #(= "local data science courses" %) :subject)))

(table/head data-with-diff)

(def prompt-response-threshold (* 60 60 12))

(-> data
    (table/order-by :timestamp)
    (table/group-by :subject)
    (table/add-column :secs-since-last #(let [timestamp (:timestamp %)]
                                          (fun/- timestamp (fun/shift timestamp 1))))
    ;; create a flag to point when the previous message was posted by the same user
    (table/add-column :same-sender-as-last? #(let [sender-id (:sender_id %)]
                                               (fun/eq sender-id (fun/shift sender-id 1))))
    (table/add-column :prompt-response? #(fun/< (:secs-since-last %)
                                                prompt-response-threshold))
    (table/add-column :next-response-prompt? #(fun/< (fun/shift (:secs-since-last %) -1)
                                                     prompt-response-threshold))
    ;; calculate the time since post by a user that is other than the current poster
    (table/add-column :active-conversation? #(tech.v3.datatype/emap
                                              ;; (fn [same-sender? secs-since-last prompt-response?]
                                              ;;   (if-not same-sender?
                                              ;;     (< secs-since-last threshold)
                                              ;;     (or (= 0 secs-since-last)
                                              ;;         (= prompt-response? true))))
                                              (fn [next-response-prompt?]
                                                (if next-response-prompt? true false))
                                              :boolean
                                              (:next-response-prompt? %)
                                              ))
    ;; mark all posts active or inactive based on time since poster switch
    ;; mark all points when a conversation is finished?
    ;; decide heuristics: how long is a break so that a conversation turns inactive
    ;; 3 hours?
    (table/ungroup)
    (table/select-rows (comp #(= % "preferred notebook") :subject))
    (table/drop-columns [:id :type :stream_id :timestamp :sender_id #_:secs-since-last :same-sender-as-last? :content])
    (vary-meta merge {:print-column-max-width 50})
    )
