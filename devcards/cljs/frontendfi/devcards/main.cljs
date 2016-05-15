(ns frontendfi.devcards.main
  (:require
    [frontendfi.views.app :as main-app])
  (:require-macros
    [devcards.core :as dc :refer [defcard deftest defcard-doc]]))

(defonce data (atom {:people/by-id      {0 {:name "Mikko" :age 19 :gender "Male" :id 0}
                                1 {:name "Pekka" :age 30 :gender "Male" :id 1}
                                2 {:name "Maija" :age 25 :gender "Female" :id 2}}
            :departments/by-id {0 {:id 0 :name "Logistics" :people #{[:people/by-id 2]}}}}))

(defcard-doc
  "### Cards for the frontend.fi demo
  These cards use the following initial data: " @data
  "Note that the data is shared among many of the cards, and is updated if
  you use the cards. This documentation does not represent the current version
  of the data.")

(defcard Root
         ""
         (fn [state owner] (main-app/app state))
         data
         {:history true
          :inspect-data true})

(defonce observed-atom
         (let [a (atom 0)]
           (js/setInterval (fn [] (swap! observed-atom inc)) 1000)
           a))

(defcard atom-observing-card observed-atom)