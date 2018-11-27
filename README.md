# alrightee

Tee for re-frame event handlers

[![Clojars Project](https://img.shields.io/clojars/v/alrightee.svg)](https://clojars.org/alrightee)

## How to use it

Install a tee to send the arguments of a function to additional other function(s) of your choice.

The original function will be called first and its return value will be returned.
The other "sink" functions will be called in the order provided.

For example, imagine a library you were using had an `init` function that didn't provide any logging but you wanted to add some.

```clojure
(ns my-app.core
  (:require [alrightee.core :as tee]
            [library.core :as lib]))

(defn- log-init-call [& args]
  (log/infof "Called %s with args %s" 'lib/init args))

(tee/tee! #'lib/init [log-init-call]

(lib/init {:some "args"})
```

You can `un-tee` a function as well to return to the original behaviour:

```clojure
(tee/un-tee! #'lib/init)
```

## re-frame

re-frame event handlers can also be tee'd. Alrightee then!

```clojure
(ns my-app.core
  (:require [alrightee.re-frame :as tee]
            [library.core :as lib]))

(re-frame/reg-event-fx
  ::log-init-call
  (fn [_ [_ & args]]
    (log/infof "Called %s with args %s" 'lib/init args)))

(tee/tee! ::lib/init [::log-init-call])

(re-frame/dispatch [::lib/init {:some "args"}])
```

The event dispatched to `::lib/init` is now tee'd and is sent to _both_ `::log-init-call` and `::lib/init`
so the library carries on behaving as normal but you get to handle the event in your code too.

As before, you can `un-tee`:

```clojure
(tee/un-tee! ::lib/init)
```

![](https://media.giphy.com/media/5hc2bkC60heU/giphy.gif)

[![CircleCI](https://circleci.com/gh/oliyh/alrightee.svg?style=svg)](https://circleci.com/gh/oliyh/alrightee)
