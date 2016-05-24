(ns frontendfi.reagent.app
  (:require [reagent.core :as r :refer [atom]]
            [ajax.core :refer [GET POST]]
            [frontendfi.models.model :as model]))

(defn project-status [departments people]
  (let [employed-count (reduce + (map (comp count :people second) @departments))
        people-count (count @people)
        jobless (- people-count employed-count)]
    (cond
      (> jobless 1) [:div.jobless-warning "Too many people without projects"]
      (= jobless 0) [:div.no-free-people "Lack of human resources"]
      :else [:div.good-project-status "Everything is awesome"])))

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
        (fn [i [_ p]]
          [:li
           {:class (if (even? i) "even" "uneven")
            :key   (str "person_in_list_" i)}
           [person p]
           (doall
             (map
               (fn [[_ d]]
                 (when-not (model/employee-in-deparment? (:id d) (:id p))
                   ^{:key (str "switch_" (:id d) "_" (:id p))}
                   [switch-dept-button! d p]))
               @departments))])
        @people-atom))]])

(defn department [dept employees]
  [:div
   (:name dept) " has " (count employees) " employees"
   [:ul
    (map
      (fn [emp]
        [:li
         {:key (str "emp_in_list_" (:name emp) (:age emp) (:name dept))}
         [person emp]
         [remove-person-from-dept-button! dept emp]])
      employees)]])

(defn departments [departments people]
  [:div
   (for [[_ d] @departments]
     ^{:key (str "department_" (:name d))}
     [department d (map (fn [emp-q] (get-in @people (rest emp-q))) (:people d))])])

(defn app [atom]
  [:div "Reagent application"
   [project-status (r/cursor atom model/department-path) (r/cursor atom model/people-path)]
   [people-list (r/cursor atom model/people-path) (r/cursor atom model/department-path)]
   [departments (r/cursor atom model/department-path) (r/cursor atom model/people-path)]])

(defn mount! [element]
  (r/render-component [app model/model] element))