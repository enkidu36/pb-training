(ns ^:figwheel-hooks pb-training.page.material-example
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [reagent-material-ui.cljs-time-utils :refer [cljs-time-utils]]
            [reagent-material-ui.colors :as colors]
            [reagent-material-ui.core.button :refer [button]]
            [reagent-material-ui.core.chip :refer [chip]]
            [reagent-material-ui.core.css-baseline :refer [css-baseline]]
            [reagent-material-ui.core.input-adornment :refer [input-adornment]]
            [reagent-material-ui.core.grid :refer [grid]]
            [reagent-material-ui.core.menu-item :refer [menu-item]]
            [reagent-material-ui.core.text-field :refer [text-field]]
            [reagent-material-ui.core.textarea-autosize :refer [textarea-autosize]]
            [reagent-material-ui.core.toolbar :refer [toolbar]]
            [reagent-material-ui.icons.add-box :refer [add-box]]
            [reagent-material-ui.icons.clear :refer [clear]]
            [reagent-material-ui.icons.face :refer [face]]
            [reagent-material-ui.pickers.date-picker :refer [date-picker]]
            [reagent-material-ui.pickers.mui-pickers-utils-provider :refer [mui-pickers-utils-provider]]
            [reagent-material-ui.styles :as styles])
  (:import (goog.i18n DateTimeSymbols_en_US)))

(set! *warn-on-infer* false)

(rf/reg-event-db
  :initialise-db
  (fn [_ _]
    {:text-state "foobar"
     :date-picker-state nil}))

(rf/reg-event-db
  :text
  (fn [db [_ text]]
    (assoc db :text-state text)))

(rf/reg-event-db
  :date-picker
  (fn [db [_ value]]
    (assoc db :date-picker-state value)))

(rf/reg-sub
  :text
  (fn [db]
    (get-in db [:text-state])))

(rf/reg-sub
  :date-picker-state
  (fn [db]
    (get-in db [:date-picker-state])))


(defn event-value
  [^js/Event e]
  (.. e -target -value))

;; Example

(def custom-theme
  {:palette {:primary   colors/purple
             :secondary colors/green}})

(defn custom-styles [{:keys [spacing] :as theme}]
  {:button     {:margin (spacing 1)}
   :text-field {:width        200
                :margin-left  (spacing 1)
                :margin-right (spacing 1)}})

(def with-custom-styles (styles/with-styles custom-styles))

(defonce text-state (r/atom "foobar"))
(defonce date-picker-state (r/atom nil))

(defn form [{:keys [classes] :as props}]
  [grid
   {:container true
    :direction "column"
    :width "50%"
    :spacing   10}

   [grid {:item true}
    [toolbar
     {:disable-gutters true}
     [button
      {:variant  "contained"
       :color    "primary"
       :class    (:button classes)
       :on-click #(rf/dispatch [:text "foobar"])}
      "Update value property"
      [add-box]]

     [button
      {:variant  "outlined"
       :color    "secondary"
       :class    (:button classes)
       :on-click #(rf/dispatch [:text ""])}
      "Reset"
      [clear]]]]

   [grid {:item true}
    [text-field
     {:value       @(rf/subscribe [:text :initialise-db])
      :label       "Text input"
      :placeholder "Placeholder"
      :helper-text "Helper text"
      :class       (:text-field classes)
      :on-change   (fn [e]
                     (rf/dispatch [:text (event-value e)] ))}]]

   [grid {:item true}
    [text-field
     {:value       @(rf/subscribe [:text :initialise-db])
      :label       "Text with Adornment"
      :placeholder "Placeholder"
      :helper-text "Helper text"
      :class       (:text-field classes)
      :on-change   (fn [e]
                     (rf/dispatch [:text (event-value e)]))
      :InputProps {:end-adornment (r/as-element [input-adornment {:position "end"} "Baz"])}}]]

   [grid {:item true}
    [text-field
     {:value       @(rf/subscribe [:text :initialise-db])
      :label       "Textarea"
      :placeholder "Placeholder"
      :helper-text "Helper text"
      :class       (:text-field classes)
      :on-change   (fn [e]
                     (rf/dispatch [:text (event-value e)] ))
      :multiline   true
      :rows        10}]]

   [grid {:item true}
    [textarea-autosize
     {:id        :textarea-autosize
      :value     @(rf/subscribe [:text :initialise-db])
      :class     (:text-field classes)
      :on-change (fn [e]
                   (rf/dispatch [:text (event-value e)] ))
      :rows-max  10}]]

   [grid {:item true}
    [text-field
     {:value       @(rf/subscribe [:text :initialise-db])
      :label       "Select"
      :placeholder "Placeholder"
      :helper-text "Helper text"
      :class       (:text-field classes)
      :on-change   (fn [e]
                     (rf/dispatch [:text (event-value e)] ))
      :select      true}
     [menu-item
      {:value 1}
      "Item 1"]
     [menu-item
      {:value 2}
      "Item 2"]]]

   [grid {:item true}
    ;; For properties that require React Node as parameter,
    ;; use r/as-element to convert Reagent hiccup forms into React elements,
    ;; or use r/create-element to directly instantiate element from React class (i.e. non-adapted React component).
    [grid {:item true}
     [chip
      {:icon  (r/as-element [face])
       :label "Icon element example, r/as-element"}]]]

   [grid {:item true}
    [date-picker {:value       @date-picker-state
                  :on-change   (fn [value]
                                 (reset! date-picker-state value))
                  :format      "MM/dd/yyyy"
                  :placeholder "Select a date"
                  :helper-text "Helper text"
                  :auto-ok     true}]]])

(defn mui-example []
  ;; fragment
  [:<>
   [css-baseline]
   ;; mui-pickers-utils-provider provides date handling utils to date and time pickers.
   ;; cljs-time-utils is an utility package that allows you to use cljs-time / goog.date date objects.
   [mui-pickers-utils-provider {:utils  cljs-time-utils
                                :locale DateTimeSymbols_en_US}
    [styles/theme-provider (styles/create-mui-theme custom-theme)
     [grid
      {:container true
       :direction "row"
       :justify   "center"}
      [grid
       {:item true
        :xs   8}
       [(with-custom-styles form)]]]]]])

