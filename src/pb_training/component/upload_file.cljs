(ns pb-training.component.upload-file
  (:require [re-frame.core :as re-frame]
            [reagent-material-ui.core.button :refer [button]]
            [reagent-material-ui.core.text-field :refer [text-field]]
            [reagent-material-ui.core.fab :refer [fab]]
            [reagent-material-ui.icons.add :refer [add]]
            [reagent-material-ui.core.input-label :refer [input-label]]
            [ajax.core :refer [GET POST]]
            [ajax.formats :as f]
            [goog.labs.format.csv :as csv]))

(defn event-files [^js/Event e]
  (.. e -currentTarget -files))

;; interceptors
(def log-interceptor
  (re-frame/->interceptor
    :id ::logger
    :after (fn [context]
             (js/console.log context)
             context)))

;; events
(re-frame/reg-event-fx
  ::load-files
  [log-interceptor]
  (fn [cofx [_ files]]
    {::load-files files
     :db          (assoc (:db cofx) :file-name (-> files (aget 0) .-name))}))

(re-frame/reg-event-fx
  ::read-file
  (fn [cofx [_ event]]
    {::read [(:db cofx) event]}))

(re-frame/reg-event-db
  ::load-file-data
  (fn [db [_ results]]
    (assoc-in db [:file :data] results)))

;; effects
(re-frame/reg-fx
  ::load-files
  (fn [files]
    (js/console.log "re-frame/reg-fx ::load-files")
    (let [file (aget files 0)
          file-name (.-name file)
          form-data (doto
                      (js/FormData.)
                      (.append "file" file file-name))]
      (js/console.log (str "loading file name: " file-name))
      (POST "http://localhost:3031/files/upload"
            {:body          form-data
             :handler       #(js/console.log (str "ok: " %))
             :headers       {"Cache-Control" "no-cache"}
             :error-handler #(js/console.log (str "error: " %))}))))

(re-frame/reg-fx
  ::read
  (fn [[db event]]
    (let [results (.. event -target -result)]
      (js/console.log "::read")
      (re-frame/dispatch [::load-file-data results]))))

;; subscriptions
(re-frame/reg-sub
  ::load-files
  (fn [db]
    (js/console.log "::load-files subscription")
    (-> db :file-name)))

(re-frame/reg-sub
  ::load-file-data
  (fn [db]
    (js/console.log (str "::file-data subscription"))
    (-> db :file :data)))

(defn upload-btn []
  [:div
   [:label {:style {:htmlFor "upload-file"}}
    [:input
     {:id        "upload-file"
      :name      "upload-file"
      :type      "file"
      :on-change #(re-frame/dispatch [::load-files (event-files %)])
      :style     {:display "none"}}]
    [fab
     {:color      "secondary"
      :size       "small"
      :component  "span"
      :aria-label "add"
      :variant    "extended"}
     [add] "Choose File"]]])

(defn upload-file []
  [:div
   [upload-btn]
   [:div
    [:p @(re-frame/subscribe [::load-files])]]])
