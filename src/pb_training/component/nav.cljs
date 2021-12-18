(ns pb-training.component.nav
  (:require [reagent-material-ui.core.app-bar :refer [app-bar]]
            [reagent-material-ui.core.toolbar :refer [toolbar]]
            [reagent-material-ui.icons.menu :refer [menu]]
            [reagent-material-ui.core.typography :refer [typography]]
            [reagent-material-ui.core.icon-button :refer [icon-button]]
            [reagent-material-ui.core.link :refer [link]]
            [reitit.core :as r]
            [reitit.frontend.easy :as rfe]))

(defn href
  "Return relative url for given route. Url can be used in HTML links..."
  ([k]
   (href k nil nil))
  ([k params]
   (href k params nil))
  ([k params query]
   (rfe/href k params query)))

(defn menu-link [route-name text active?]
  [link {:href  (href route-name)
         :color "inherit"
         :style {:paddingLeft 20 :font-weight (if active? 600 "inherit")}}
   text])

(defn navbar [{:keys [router current-route]}]
  [:<>
   [app-bar {:position "sticky"}
    [toolbar {}
     [icon-button {:edge "start" :color "inherit"}
      [menu]]
     [typography {:variant "h5"}
      (for [route-name (r/route-names router)
            :let [route (r/match-by-name router route-name)
                  text (-> route :data :link-text)
                  active? (= route-name (-> current-route :data :name))]]
        ^{:key route-name} [menu-link route-name text active?])]]]])


