(ns frontendfi.devcards.reagent-cards
  (:require [devcards.core]
            [frontendfi.reagent.app :as app]
            [frontendfi.models.model :as model]
            [reagent.core :as r])
  (:require-macros
    [devcards.core :as dc :refer [defcard deftest defcard-doc defcard-rg]]
    [cljs.test :refer [testing is]]))

(defonce data {:people/by-id      {0 {:name "Mikko" :age 19 :gender "Male" :id 0}
                                   1 {:name "Pekka" :age 30 :gender "Male" :id 1}
                                   2 {:name "Maija" :age 25 :gender "Female" :id 2}}
               :departments/by-id {0 {:id 0 :name "Logistics" :people #{[:people/by-id 2]}}}})

(defcard-doc
  "### Cards for the frontend.fi demo
  These cards use the following initial data: " data
  "Note that the data is shared among many of the cards, and is updated if
  you use the cards. This documentation does not represent the current version
  of the data.")

(reset! model/model data)

(defcard-rg Root
            "This card has `:inspect-data true` and `:history true`"
            (fn [state _] (app/app state))
            model/model
            {:history true
             :inspect-data true})

(defcard-rg Lack-of-resources
            (fn [state _ ] [app/project-status
                            (r/cursor state model/department-path)
                            (r/cursor state model/people-path)])
            (r/atom model/initial))

(defcard-rg Too-many-people
            (fn [state _ ] [app/project-status
                            (r/cursor state model/department-path)
                            (r/cursor state model/people-path)])
            (r/atom {:people/by-id {0 {:name "Mikko", :age 19, :gender "Male", :id 0},
                                    1 {:name "Pekka", :age 30, :gender "Male", :id 1},
                                    2 {:name "Maija", :age 25, :gender "Female", :id 2}},
                     :departments/by-id {0 {:id 0, :name "Logistics", :people #{[:people/by-id 2]}}}})
            {:inspect-data false})

(defcard-rg Everything-is-awesome
            (fn [state _ ] [app/project-status
                            (r/cursor state model/department-path)
                            (r/cursor state model/people-path)])
            (r/atom {:people/by-id {0 {:name "Mikko", :age 19, :gender "Male", :id 0},
                                    2 {:name "Maija", :age 25, :gender "Female", :id 2}},
                     :departments/by-id {0 {:id 0, :name "Logistics", :people #{[:people/by-id 2]}}}})
            {:inspect-data false})