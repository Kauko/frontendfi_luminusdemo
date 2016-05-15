(ns frontendfi.views.app
  (:require [rum.core :as rum]
            [frontendfi.models.model :as model]))

(rum/defc person < rum/static [person]
          [:li (:name person) ", " (:age person)])

(rum/defc people < rum/reactive [people]
          (let [people (map second (sort (rum/react people)))]
            [:div
             [:p "We have " (str (count people)) " employees in total"]
             [:ul
              (for [p people]
                (rum/with-key (person p) (str "person_" (:id p))))]]))

(rum/defc dept < rum/static [d employees]
          [:div
           [:p (:name d) " has " (count (:people d)) " employees."]
           [:ul
            (for [p employees]
              (rum/with-key (person p) (str "employee_" (:id p))))]])

(rum/defc departments < rum/reactive [atom]
          (let [model (rum/react atom)
                depts (map second (sort (get-in model model/department-path)))]
            [:div
             (for [d depts]
               (rum/with-key (dept d (sort-by :id (map #(get-in model %) (:people d))))
                             (str "dept_" (:id d))))]))

(rum/defc app [atom]
          [:div
           (people (rum/cursor atom model/people-path))
           (departments atom)])

#?(:cljs
   (defn mount! [mount-el]
     (model/init! {:on-success #(rum/mount (app model/model) mount-el)})))