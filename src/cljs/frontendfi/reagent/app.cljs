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

(defn people-list [people-atom]
  [:ul
   (map-indexed
     (fn [i [_ p]]
       [:li
        {:class (if (even? i) "even" "uneven")
         :key (str "person_in_list_" i)}
        (str (:name p) ", " (:age p))])
     @people-atom)])

(defn department [dept employees]
  [:div
   (:name dept) " has " (count employees) " employees"
   [:ul
    (map
      (fn [emp]
        [:li
         {:key (str "emp_in_list_" (:name emp) (:age emp) (:name dept))}
         (str (:name emp) ", " (:age emp))])
      employees)]])

(defn departments [departments people]
  [:div
   (for [[_ d] @departments]
     ^{:key (str "department_" (:name d))}
     [department d (map (fn [emp-q] (get-in @people (rest emp-q))) (:people d))])])

(defn app [atom]
  [:div "Reagent application"
   [project-status (r/cursor atom model/department-path) (r/cursor atom model/people-path)]
   [people-list (r/cursor atom model/people-path)]
   [departments (r/cursor atom model/department-path) (r/cursor atom model/people-path)]])

(defn mount! [element]
  (r/render-component [app model/model] element))