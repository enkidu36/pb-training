(ns pb-training.component.copyright
  (:require
    [reagent-material-ui.core.typography :refer [typography]]
    [reagent-material-ui.core.link :refer [link]]
    [goog.string :as gstring]))

(defn copyright []
  [typography {:variant "h5" :color "inherit"}
   (str "Copyright " (gstring/unescapeEntities "&copy; "))
   [link {:color "inherit"
          :href  "https://material-ui.com"}
    "P-Branes training"]
   " 2021"])

