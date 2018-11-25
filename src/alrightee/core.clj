(ns alrightee.core)

(defn tee [source-var sinks]
  (alter-var-root source-var (fn [source-fn]
                               (with-meta
                                 (fn [& args]
                                   (let [result (apply source-fn args)]
                                     (doseq [f sinks]
                                       (apply f args))
                                     result))
                                 {:original source-fn}))))

(defn un-tee [tee-var]
  (when-let [original (:original (meta @tee-var))]
    (alter-var-root tee-var (constantly original))))
