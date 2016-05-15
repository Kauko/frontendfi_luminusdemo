(ns frontendfi.views.app
  (:require [rum.core :as rum]
            [frontendfi.models.model :as model]))

(rum/defc remove-person-from-dept-button [dept person]
          [:button {:on-click #(model/remove-employee-from-department!
                                (:id dept)
                                (:id person))}
           "X"])

(rum/defc switch-dept-button [dept person]
          [:button {:on-click #(model/switch-employees-department! (:id dept) (:id person))}
           (str "Add to " (:name dept))])

(rum/defc person < rum/static [person]
          [:span (:name person) ", " (:age person)])

(rum/defc people < rum/reactive [people departments]
          (let [people (map second (sort (rum/react people)))
                departments (map second (sort (rum/react departments)))]
            [:div
             [:p "We have " (str (count people)) " employees in total"]
             [:ul
              (for [p people]
                [:li
                 {:key (str "person_info" (:id p))}
                 (rum/with-key (person p) (str "person_" (:id p)))
                 (for [d departments]
                   (when-not (model/employee-in-deparment? (:id d) (:id p))
                     (rum/with-key
                       (switch-dept-button d p)
                       (str "switch_" (:id d) "_" (:id p)))))])]]))

(rum/defc dept < rum/static [d employees]
          [:div
           [:p (:name d) " has " (count (:people d)) " employees."]
           [:ul
            (for [p employees]
              [:li
               {:key (str "dept_info_" (:id p))}
               (rum/with-key (person p) (str "employee_" (:id p)))
               (rum/with-key (remove-person-from-dept-button d p)
                             (str "remove_emp_btn_" (:id p)))])]])

(rum/defc departments < rum/reactive [atom]
          (let [model (rum/react atom)
                depts (map second (sort (get-in model model/department-path)))]
            [:div
             (for [d depts]
               (rum/with-key (dept d (sort-by :id (map #(get-in model %) (:people d))))
                             (str "dept_" (:id d))))]))

(rum/defc app [atom]
          [:div
           (people (rum/cursor atom model/people-path) (rum/cursor atom model/department-path))
           (departments atom)])

#?(:cljs
   (defn mount! [mount-el]
     (model/init! {:on-success #(rum/mount (app model/model) mount-el)})))