(ns alrightee.core-test
  (:require [alrightee.core :as tee]
            [re-frame.core :as re-frame]
            [re-frame.db :refer [app-db]]
            [day8.re-frame.test :refer [run-test-sync]]
            #?(:clj [clojure.test :refer [deftest testing is]]
               :cljs [cljs.test :refer-macros [deftest testing is]])))

(defn- reg-recorder [handler-name]
  (re-frame/reg-event-db
   handler-name
   (fn [db [_ message]]
     (assoc db handler-name message))))

(deftest dispatch-test
  (testing "can tee to multiple destinations"
    (run-test-sync

     (doseq [handler-name [::one ::two ::three]]
       (reg-recorder handler-name))

     (tee/reg-tee ::message [::one ::two ::three])
     (re-frame/dispatch [::message "hello!"])

     (is (= {::one "hello!"
             ::two "hello!"
             ::three "hello!"}
            @app-db)))))

(deftest acceptance-test
  (testing "can replace an existing event handler with a tee"
    (run-test-sync

     (re-frame/reg-event-fx ::start (fn [{:keys [db]} [_ message]]
                                      {:db (assoc db ::start message)
                                       :dispatch [::finish message]}))

     (reg-recorder ::finish)
     (reg-recorder ::teed-handler)

     (tee/tee ::finish [::teed-handler])

     (re-frame/dispatch [::start "hi"])

     (is (= {::start "hi"
             ::finish "hi"
             ::teed-handler "hi"}
            @app-db)))))
