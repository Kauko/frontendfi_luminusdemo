(ns frontendfi.views.app
  (:require [rum.core :as rum]
            [frontendfi.models.model :as model]))

(rum/defc person < rum/static [person]
          [:div (:name person)])

(rum/defc people < rum/reactive [people]
          (let [people (map second (sort (rum/react people)))]
            [:div
            [:p (str (count people))]
            (person (first people))]))

(rum/defc app [atom]
          [:div "This is my APP"
           (people (rum/cursor atom model/people-path))])

#?(:cljs
   (defn mount! [mount-el]
     (model/init! {:on-success #(rum/mount (app model/model) mount-el)})))