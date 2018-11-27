(ns alrightee.core)

(defn tee
  "Tees the input to source out to sinks in the order they are provided, calling source first and returning its return value"
  [source sinks]
  (fn [& args]
    (let [result (apply source args)]
      (doseq [f sinks]
        (apply f args))
      result)))

(defn tee!
  "Replaces the function at source-var with a teed version"
  [source-var sinks]
  (alter-var-root source-var (fn [source-fn]
                               (with-meta
                                 (tee source-fn sinks)
                                 {:original source-fn}))))

(defn un-tee!
  "Reverts the teed function at source-var to its original function"
  [tee-var]
  (when-let [original (:original (meta @tee-var))]
    (alter-var-root tee-var (constantly original))))
