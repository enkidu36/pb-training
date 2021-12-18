(ns pb-training.component.footer
  (:require [pb-training.component.copyright :refer [copyright]]
            [reagent-material-ui.core.app-bar :refer [app-bar]]
            [reagent-material-ui.core.icon-button :refer [icon-button]]
            [reagent-material-ui.icons.menu :refer [menu]]
            [reagent-material-ui.core.grid :refer [grid]]
            [reagent-material-ui.core.toolbar :refer [toolbar]]))

(defn footer []
  [:<>
   [app-bar {:position "relative"}
    [toolbar
     [grid {:container true :justify "flex-end"}
      [grid {:item true :xs 4}
       [copyright]]]]]])
