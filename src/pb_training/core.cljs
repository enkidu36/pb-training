(ns ^:figwheel-hooks pb-training.core
  (:require [reagent.dom :as reagent-dom]
            [re-frame.core :as re-frame]
            [reitit.core :as r]
            [reitit.coercion.spec :as rss]
            [reitit.frontend :as rf]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]
            [reagent-material-ui.colors :as colors]
            [reagent-material-ui.core.css-baseline :refer [css-baseline]]
            [reagent-material-ui.core.grid :refer [grid]]
            [reagent-material-ui.styles :as styles]
            [pb-training.component.nav :refer [navbar]]
            [pb-training.page.abilities-table :refer [abilities-table]]
            [pb-training.page.cards :refer [cards]]
            [pb-training.page.sandbox :refer [sandbox]]
            [pb-training.page.material-example :refer [mui-example]]
            [pb-training.component.footer :refer [footer]])
  (:import (goog.i18n DateTimeSymbols_en_US)))

(set! *warn-on-infer* false)

;;; Events ;;;

(re-frame/reg-event-db ::initialize-db
                       (fn [db _]
                         (if db
                           db
                           {:current-route nil})))

(re-frame/reg-event-fx ::push-state
                       (fn [db [_ & route]]
                         {:push-state route}))

(re-frame/reg-event-db ::navigated
                       (fn [db [_ new-match]]
                         (let [old-match (:current-route db)
                               controllers (rfc/apply-controllers (:controllers old-match) new-match)]
                           (assoc db :current-route (assoc new-match :controllers controllers)))))

;;; Subscriptions ;;;

(re-frame/reg-sub ::current-route
                  (fn [db]
                    (:current-route db)))

;;; Effects ;;;

;; Triggering navigation from events.

(re-frame/reg-fx :push-state
                 (fn [route]
                   (apply rfe/push-state route)))

;;; Routes ;;;

(def routes
  ["/"
   ["sandbox"
    {:name ::sandbox
     :view #'sandbox
     :link-text "Sandbox"}]
   ["home"
    {:name      ::home
     :view      #'cards
     :link-text "Home"
     :controllers
                [{;; Do whatever initialization needed for home page
                  ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
                  :start (fn [& params] (js/console.log "Entering home page"))
                  ;; Teardown can be done here..
                  :stop  (fn [& params] (js/console.log "Leaving home page"))}]}]
   ["abilities"
    {:name      ::abilities
     :view      #'abilities-table
     :link-text "Abilities"
     :controllers
                [{:start (fn [& params] (js/console.log "Entering abilities"))
                  :stop  (fn [& params] (js/console.log "Leaving abilities"))}]}]
   ["mui-example"
    {:name      ::mui-example
     :view      #'mui-example
     :link-text "MUI-Example"}]])

(defn on-navigate [new-match]
  (when new-match
    (re-frame/dispatch [::navigated new-match])))

(def router
  (rf/router
    routes
    {:data {:coercion rss/coercion}}))

(defn init-routes! []
  (js/console.log "initializing routes")
  (rfe/start!
    router
    on-navigate
    {:use-fragment true}))

;; main template

(def custom-theme
  {:palette {:primary   colors/purple
             :secondary colors/green
             :text      colors/yellow}})

(defn custom-styles [{:keys [spacing] :as theme}]
  {:button {:margin (spacing 1)}})

(defn router-component [{:keys [router]}]
  (let [current-route @(re-frame/subscribe [::current-route])]
    [:div
     [navbar {:router router :current-route current-route}]
     [:div {:style {:marginTop 10 :padding 20}}
      (when current-route
        [(-> current-route :data :view)])]
     [footer]]))

(defn main []
  [:<>
   [css-baseline]
   [styles/theme-provider (styles/create-mui-theme custom-theme)
    [router-component {:router router}]]])

;;; Setup ;;;;

(def debug? ^boolean goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

(defn ^{:after-load true, :dev/after-load true}
  mount []
  (reagent-dom/render [main]
                      (.getElementById js/document "app"))
  (let [current-route @(re-frame/subscribe [::current-route])]
    (when (nil? current-route)
      (js/console.log "initialize to home")
      (re-frame/dispatch-sync [::push-state ::home]))))

(defn ^:export init []
  (re-frame/clear-subscription-cache!)
  (re-frame/dispatch-sync [::initialize-db])
  (dev-setup)
  (init-routes!)
  (mount))