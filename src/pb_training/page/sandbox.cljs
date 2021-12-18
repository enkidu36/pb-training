(ns pb-training.page.sandbox
  (:require [pb-training.component.upload-file :as uf]))

(defn sandbox []
  [:<>
    [uf/upload-file]])


