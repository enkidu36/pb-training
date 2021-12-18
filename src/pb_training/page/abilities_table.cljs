(ns pb-training.page.abilities-table
  (:require
    [reagent.core :as r]
    [reagent-material-ui.core.css-baseline :refer [css-baseline]]
    [reagent-material-ui.colors :as colors]
    [reagent-material-ui.core.grid :refer [grid]]
    [reagent-material-ui.core.paper :refer [paper]]
    [reagent-material-ui.styles :as styles]
    [reagent-material-ui.core.table :refer [table]]
    [reagent-material-ui.core.table-body :refer [table-body]]
    [reagent-material-ui.core.table-cell :refer [table-cell]]
    [reagent-material-ui.core.table-container :refer [table-container]]
    [reagent-material-ui.core.table-head :refer [table-head]]
    [reagent-material-ui.core.table-row :refer [table-row]]
    [clojure.string :as string]))

(def adaptions
  [[1 "Increased plasma volume" 0 1 2 3 4 1 0]
   [2 "Increased muscle mitochondrial enzyme" 0 2 3 4 2 1 0]
   [3 "Increased muscle glycogen storage" 2 4 3 2 1 0 0]
   [4 "Hypertrophy of slow-twitch muscle fibers" 1 2 2 3 1 0 0]
   [5 "Increased muscular capillarization" 1 2 2 3 1 0 0]
   [6 "Interconversion of fast-twitch muscle fibers (Type IIx -> Type IIa)" 2 3 3 2 1 0 0]
   [7 "Increased stroke volume/maximal cardiac output" 1 2 3 4 1 0 0]
   [8 "Increased VO2 max" 1 2 3 4 1 0 0]
   [9 "Increased muscle high-energy phosphate (ATP/PCr) stores" 0 0 0 0 0 1 2]
   [10 "Increased anaerobic capacity" 0 0 0 0 1 3 1]
   [11 "Hypertrophy of fast-twitch fibers" 0 0 0 0 0 1 2]
   [12 "Increased neuromuscular power" 0 0 0 0 0 1 3]])

(def headers ["Adaptions" "Active Recovery" "Endurance" "Tempo" "Lactate Threshold" "VO2 Max" "Anaerobic Capacity" "Neuro-muscular Power"])
(def custom-theme
  {:palette {:primary   colors/purple
             :secondary colors/green}})

(def custom-styles
  {:table-container {:max-height 1000}
   :paper           {:width           "95%"
                     :padding         "10px"
                     :backgroundColor "#e3e7e7f2"}

   :table           {}
   :header          {:backgroundColor "black"
                     :color           "white"}
   :body            {:backgroundColor "gray"
                     :color           "light-gray"}})

(def with-custom-styles (styles/with-styles custom-styles))

(defn render-head [headers classes]
  [table-row
   (for [index (range 0 (count headers))]
     (let [text-align (if (= 0 index) "left" "center")
           header (nth headers index)]
       ^{:key header} [table-cell {:align text-align
                                   :class (:header classes)} header]))])

(defn number->text [value]
  (string/join (map #(conj "+") (range 0 value))))

(defn render-cell [value classes]
  [:<>
   [table-cell {:align (if (number? value) "center" "left")
                :class (:body classes)}
    (if (number? value)
      (number->text value) value)]])

(defn render-rows [adaptions classes]
  [:<>
   (for [row adaptions]
     (let [row-id (nth row 0)]
       ^{:key row-id}
       [table-row
        (for [index (range 1 (count row))]
          ^{:key index}
          [render-cell (nth row index) classes])]))])

(defn adaption-table [{:keys [classes] :as props}]
  [table-container {:align "center"
                    :class (:table-container classes)}
   [paper {:class     (:paper classes)
           :variant   "elevation"
           :elevation 12}
    [table {:class (:table classes)
            :stickyHeader true}
     [table-head [render-head headers classes]]
     [table-body [render-rows adaptions classes]]]]])

(defn abilities-table []
  [:<>
   [styles/theme-provider (styles/create-mui-theme custom-theme)
    [grid {:container true
           :spacing   2}
     [grid {:item    true
            :lg      12}
      [(with-custom-styles adaption-table)]]]]])


