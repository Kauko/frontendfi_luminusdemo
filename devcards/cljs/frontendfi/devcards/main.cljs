(ns frontendfi.devcards.main
  (:require
    [frontendfi.views.app :as main-app]
    [frontendfi.devcards.reagent-cards :as rc]
    [frontendfi.models.model :as model])
  (:require-macros
    [devcards.core :as dc :refer [defcard deftest defcard-doc]]
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

(defcard Root
         "This card has `:inspect-data true`"
         (fn [state owner] (main-app/app state))
         model/model
         {:history true
          :inspect-data true})

(defcard-doc
  "Devcards can even show up-to-date source code"
  (dc/mkdn-pprint-source main-app/app))

(deftest
  first-testers
  "## Running tests\n   Devcards can even run tests for you"
  (testing
    "good stuff"
    (is (= (+ 3 4 55555) 5) "Testing the adding")
    (is (= (+ 1 0 0 0) 1) "This should work")
    (is (= 1 3))
    (is false)
    (is (throw "heck"))
    (is (js/asdf)))
  "It would probably make sense to have these in another namespace though."
  (testing
    "More tests"
    (is (= (+ 1 0 0 0) 1))
    (is (= (+ 3 4 55555) 4))
    (is false)
    (testing
      "Nested testing-block"
      (is (= (+ 1 0 0 0) 1))
      (is (= (+ 3 4 55555) 4))
      (is false))))

#_(defonce observed-atom
           (let [a (atom 0)]
             (js/setInterval (fn [] (swap! observed-atom inc)) 1000)
             a))

#_(defcard atom-observing-card observed-atom)