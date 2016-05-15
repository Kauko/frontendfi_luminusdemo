(ns frontendfi.models.model
  (:require [clojure.set :as set]
            #?(:cljs [ajax.core :refer [GET POST]])))

(def initial
  {:people/by-id {}
   :departments/by-id {0 {:name "Software" :people #{} :id 0}
                       1 {:name "Marketing" :people #{} :id 1}
                       2 {:name "HR" :people #{} :id 2}}})

(def people-path [:people/by-id])
(def department-path [:departments/by-id])
(def employees-path [:people])
(defn dept-employees-path [dept-it]
  (concat (conj department-path dept-it) employees-path))

(def model (atom initial))

(defn next-id [path]
  (let [ids (keys (get-in @model path))]
    (if ids
      (inc (apply max ids))
      0)))

(def next-people-id (partial next-id people-path))

(defn take-random [coll]
  (nth coll (rand-int (count coll))))

(defn random-name []
  (take-random ["Bob" "Mary" "George" "Jane" "Michael"
                "Lee" "Tom" "Andrew" "Nick" "Emma" "Anna"
                "Tracy" "Elissa"]))

(defn random-gender []
  (take-random ["Male" "Female"]))

(defn random-age []
  (take-random (range 18 60)))

(defn generate-person
  ([] (generate-person {:name (random-name)
                        :gender (random-gender)
                        :age (random-age)}))
  ([{:keys [name gender age]}]
   {:name       (or name (random-name))
    :gender     (or gender (random-gender))
    :age        (or age (random-age))}))

(defn add-people!
  ([people] (add-people! people false))
  ([people overwrite?]
   (let [new-ids (take (count people) (iterate inc (next-people-id)))
         people-with-ids (into {}
                               (map (fn [[generated-id person]]
                                      (let [id (or (:id person) generated-id)]
                                        {id (assoc person :id id)})))
                               (zipmap new-ids people))]
     (swap! model update-in people-path
            (partial merge-with (fn [old new] (if overwrite? new old)))
            people-with-ids)

     (if overwrite?
       people-with-ids
       (into {}
             (filter
               (fn [[id _]]
                 (some #(= % id) new-ids)))
             people-with-ids)))))

(defn add-generated-people!
  ([] (add-generated-people! 5))
  ([n]
   (add-people! (repeatedly n generate-person))))

(defn remove-employee-from-department! [dept-id id]
  (swap! model update-in (dept-employees-path dept-id) disj (conj people-path id)))

(defn add-employee-to-department! [dept-id id]
  (swap! model update-in (dept-employees-path dept-id) conj (conj people-path id)))

(defn switch-employees-department! [dept-id id]
  (doseq [[dept-id _] (get-in @model department-path)]
    (remove-employee-from-department! dept-id id))
  (add-employee-to-department! dept-id id))

(defn department-employees [dept-id]
  (get-in @model (dept-employees-path dept-id)))

(defn people-in-departments []
  (apply set/union (map (fn [[_ department]] (get-in department employees-path)) (get-in @model department-path))))

(defn make-sure-everyone-has-a-department! []
  (let [people-ids (set (keys (get-in @model people-path)))
        department-ids (keys (get-in @model department-path))
        departmentless (set/difference people-ids (map second (people-in-departments)))]
    (doseq [person-id departmentless]
      (add-employee-to-department! (take-random department-ids) person-id))))

#?(:clj
   (defn init! [_]
     (reset! model initial)
     (add-generated-people! 10)
     (make-sure-everyone-has-a-department!)
     @model))

#?(:cljs
   (defn init! [{:keys [on-success]}]
     (GET "/api/model"
          {:handler (fn [result]
                      (js/console.log (str "The result is: " (pr-str result)))
                      (reset! model result)
                      (on-success))})))

(defn refresh! []
  #?(:clj
     (do (add-generated-people! 3)
         (make-sure-everyone-has-a-department!)
         @model))
  #?(:cljs
     (GET "/api/refresh"
          {:handler (fn [result] (reset! model result))})))