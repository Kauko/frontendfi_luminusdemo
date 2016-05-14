(ns frontendfi.views.app
  (:require [rum.core :as rum]))

(rum/defc app []
          [:div "This is my APP"])

#?(:cljs
   (defn mount! [mount-el]
     (rum/mount (app) mount-el)))