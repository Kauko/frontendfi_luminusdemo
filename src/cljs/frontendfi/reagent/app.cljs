(ns frontendfi.reagent.app
  (:require [reagent.core :as r :refer [atom]]
            [ajax.core :refer [GET POST]]
            [frontendfi.models.model :as model]))

(defn project-status [departments people]
  ;; departments: [{:name "" :people [{} {}]} {:name "" ...}]
  ;; people: [{:name "" :age} {} {} {}]
  [:div "Not implemented!"])

(defn remove-person-from-dept-button! [dept person]
  [:button {:on-click #(model/remove-employee-from-department!
                        (:id dept)
                        (:id person))}
   "X"])

(defn switch-dept-button! [dept person]
    [:button {:on-click #(model/switch-employees-department! (:id dept) (:id person))}
     (str "Add to " (:name dept))])

(defn person [person]
  [:span (:name person) ", " (:age person)])

(defn people-list [people-atom departments]
  [:div
   [:p "We have " (count @people-atom) " employees in total"]
   [:ul
    (doall
      (map-indexed
        (fn [i p]
          [:li
           {:class (if (even? i) "even" "uneven")
            :key   (str "person_in_list_" i)}
           [person p]
           (doall
             (map
               (fn [d]
                 (when-not (model/employee-in-deparment? (:id d) (:id p))
                   ^{:key (str "switch_" (:id d) "_" (:id p))}
                   [switch-dept-button! d p]))
               @departments))])
        @people-atom))]])

(defn department [dept]
  [:div
   (:name dept) " has " (count (:people dept)) " employees"
   [:ul
    (map
      (fn [emp]
        [:li
         {:key (str "emp_in_list_" (:name emp) (:age emp) (:name dept))}
         [person emp]
         [remove-person-from-dept-button! dept emp]])
      (:people dept))]])

(defn departments [departments people]
  [:div
   (for [d @departments]
     ^{:key (str "department_" (:name d))}
     [department d])])

(defn app [atom]
  [:div "Reagent application"
   [project-status (r/track model/materialised-departments atom) (r/track model/materialised-people atom)]
   [people-list (r/track model/materialised-people atom) (r/track model/materialised-departments atom)]
   [departments (r/track model/materialised-departments atom) (r/track model/materialised-people atom)]])

(defn mount! [element]
  (r/render-component [app model/model] element))