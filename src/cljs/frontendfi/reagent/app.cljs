(ns frontendfi.reagent.app
  (:require [reagent.core :as r :refer [atom]]
            [ajax.core :refer [GET POST]]
            [frontendfi.models.model :as model]))


(defn people-list [people-atom]
  [:ul
   (map-indexed
     (fn [i p]
       [:li
        {:class (if (even? i) "even" "uneven")
         :key (str "person_in_list_" (:name p))}
        (str (:name p) ", " (:age p))])
     @people-atom)])

(defn app [atom]
  [:div "Reagent application"
   [people-list (r/cursor atom model/people-path)]])

(defn mount! [element]
  (r/render-component [app model/model] element))