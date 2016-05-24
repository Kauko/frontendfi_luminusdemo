(ns frontendfi.reagent.app
  (:require [reagent.core :as r]))

(defn app []
  [:div "Reagent application"])

(defn mount! [element]
  (r/render-component [app] element))