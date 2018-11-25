;; This test runner is intended to be run from the command line
(ns alrightee.test-runner
  (:require
   [alrightee.re-frame-test]
   [figwheel.main.testing :refer [run-tests-async]]))

(defn -main [& args]
  (run-tests-async 5000))
