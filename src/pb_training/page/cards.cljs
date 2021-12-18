(ns pb-training.page.cards
  (:require
    [reagent-material-ui.core.grid :refer [grid]]
    [reagent-material-ui.core.card :refer [card]]
    [reagent-material-ui.core.button :refer [button]]
    [reagent-material-ui.core.card-action-area :refer [card-action-area]]
    [reagent-material-ui.core.card-actions :refer [card-actions]]
    [reagent-material-ui.core.card-content :refer [card-content]]
    [reagent-material-ui.core.card-media :refer [card-media]]
    [reagent-material-ui.core.button]
    [reagent-material-ui.core.container :refer [container]]
    [reagent-material-ui.core.box :refer [box]]
    [reagent-material-ui.core.typography :refer [typography]]
    [reagent-material-ui.core.collapse :refer [collapse]]
    [reagent-material-ui.styles :as styles]))

(def lorem-ipsum "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")

(def posts [{:title "Post One" :excerpt lorem-ipsum :image "https://www.motorbiscuit.com/wp-content/uploads/2020/06/2020-Audi-RS6-Avant-1024x742.jpg"}
            {:title "Audi RS6" :excerpt "hello" :image "https://th.bing.com/th/id/OIP.9O_UHE0ss9zMgSMfd_pdggHaEA?w=306&h=180&c=7&o=5&dpr=1.25&pid=1.7"}
            {:title "BMC Team Machine" :excerpt lorem-ipsum :image "https://cdn.shopify.com/s/files/1/2318/5263/products/BRD19889_PH1_01_800x800.jpg?v=1609175925"}
            {:title "BMC Time Machine" :excerpt lorem-ipsum :image "https://contenderbicycles.com/wp-content/uploads/2018/06/2019-BMC-Timemachine-Road-01-One.jpg"}
            {:title "BMC Team Machine" :excerpt lorem-ipsum :image "https://contenderbicycles.com/wp-content/uploads/2018/06/2019-BMC-Timemachine-Road-01-One.jpg"}
            {:title "Philosoraptor" :excerpt lorem-ipsum :image "https://bit.ly/2WNi2Ml"}])


(defn custom-styles [{:keys [spacing] :as theme}]
  {:grid      {}
   :grid-item {}
   :card      {}
   :media     {}})

(def with-custom-styles (styles/with-styles custom-styles))

(defn card-grid [{:keys [classes] :as props}]
  [grid {:container true
         :class     (:grid classes)
         :direction "row"
         :spacing   3
         :justify   "left"}
   (for [post posts]
     ^{key (:title post)}
     [grid {:item true :xs 3 :class (:grid-item classes)}
      [card {:raised true :class (:card classes)}
       [card-action-area
        [card-media {:component "img"
                     :alt       "Reptile"
                     :title     "Reptile"
                     :class     (:media classes)
                     :image     (:image post)}]
        [card-content
         [typography {:gutterBottom true
                      :variant      "h5"
                      :component    "h3"}
          (:title post)]
         [typography {:component "p"
                      :variant   "body"}
          (:excerpt post)]]]
       [card-actions
        [button {:size "small" :color "primary"} "Share"]
        [button {:size "small" :color "primary"} "Learn More"]]
       [collapse {:in false :timeout "auto" :unmountOnExit true}
        [card-content
         [typography {:variant "p"} "This is a test"]]]]])])

(defn cards []
   [(with-custom-styles card-grid)])
