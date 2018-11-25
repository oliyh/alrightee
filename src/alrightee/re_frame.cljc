(ns alrightee.re-frame
  (:require [re-frame.core :as re-frame]
            [re-frame.registrar :as reg]))

(defn reg-tee [source sinks]
  (re-frame/reg-event-fx
   source
   (fn [_ [_ & args]]
     {:dispatch-n (mapv (fn [sink]
                          (vec (cons sink args)))
                        sinks)})))

(defn- ->new-source-name [source]
  (keyword (namespace source) (str (name source) "-tee")))

(defn tee [source sinks]
  (let [original-handler (get-in @reg/kind->id->handler [:event source])
        new-source-name (->new-source-name source)]
    (reg/register-handler :event new-source-name original-handler)
    (reg-tee source (cons new-source-name sinks))))

(defn un-tee [source]
  (let [new-source-name (->new-source-name source)
        original-handler (get-in @reg/kind->id->handler [:event new-source-name])]
    (reg/register-handler :event source original-handler)))
