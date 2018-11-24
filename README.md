# alrightee

Tee for re-frame event handlers

## How to use it

Imagine you have a re-frame application and you are using a library which itself is implemented in re-frame.

```clojure
(ns library.core
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ message]]
    {:db (assoc db ::starting? true)
     :dispatch [::on-start message]}))

(re-frame/reg-event-db
  ::on-start
  (fn [db [_ message]]
    (-> db
      (assoc ::starting? false)
      (assoc ::message message))))
```

Sometimes to organise the flow of events in your app, or to write a test, you want to capture the `::on-start` event.

re-frame doesn't expose its core interceptor stack so this can be difficult... until now. Alrightee then!

```clojure
(ns my-app.core
  (:require [alrightee.core :as tee]
            [library.core :as lib]))

(re-frame/reg-event-fx
  ::lib-has-started
  (fn [_ [_ message]]
    ;; receives the same event as ::lib/on-start
    ))

(tee/tee ::lib/on-start [::lib-has-started])

(re-frame/dispatch [::lib/init "hello!"])
```

The event dispatched to `::lib/on-start` is now tee'd and is sent to _both_ `::lib-has-started` and `::lib/on-start`
so the library carries on behaving as normal but you get to handle the event in your code too.

![](https://media.giphy.com/media/5hc2bkC60heU/giphy.gif)
