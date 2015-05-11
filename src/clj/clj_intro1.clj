(use 'mikera.image.core)
(-> (load-image-resource "logo.png")
    show
    (.setAlwaysOnTop true))








;;;;;;;;;;;;;;;;;;;;
;; 1. Live coding ;;
;;;;;;;;;;;;;;;;;;;;
(* (+ 2 3) 3)

(+ 1 1 3 4)



















;;;;;;;;;;;;;;;;;;;;;
;; 2. Java interop ;;
;;;;;;;;;;;;;;;;;;;;;
(System/currentTimeMillis)
(get (System/getProperties) "java.version")

(filter (fn [k] (.startsWith k "java")) (keys (System/getProperties)))

(new java.util.Date)

(org.apache.commons.math3.util.ArithmeticUtils/addAndCheck Long/MAX_VALUE 1)
















;;;;;;;;;;;;;;;;;;;;;
;; 3. Data as Code ;;
;;;;;;;;;;;;;;;;;;;;;

; vector
[1 2 3 5]

; map
{:key1 "foo" :key2 "bar"}

; set
#{:a :b :c}

; seq
'(1 2 3)












;;;;;;;;;;;;;;;;;;;;;
;; 4. Code as Data ;;
;;;;;;;;;;;;;;;;;;;;;
(fn [x] (* x x))
((fn [x] (* x x)) 4)


(def sqr (fn [x] (* x x)))
(defn sqr [x] (* x x))

(sqr 5)

((identity sqr) 2)







; eval
'(+ 1 1)
(eval '(+ 1 1))

(read-string "(+ 1 1)")
(eval (read-string "(+ 1 1)"))






; macros

; (+ 1 1) -> (1 1 +)
(defmacro postfix-notation [expression]
  (conj (butlast expression) (last expression)))

(macroexpand '(postfix-notation (1 2 +)))

(postfix-notation
 (1 2 +))

(macroexpand
 '(with-open [in-stream (clojure.java.io/input-stream "resources/textfile.txt")]
    (.available in-stream)))

(with-open [in-stream (clojure.java.io/input-stream "resources/textfile.txt")]
    (.available in-stream))


(if (= 1 2)
  :blue
  :red)


(loop [n 11]
  (when (not (= 0 n))
    (println "hey")
    (Thread/sleep 200)
    (recur (dec n))))
(dotimes [n 10] (println "hey"))














;;;;;;;;;;;;;;;;;;;;;
;; 5. Immutability ;;
;;;;;;;;;;;;;;;;;;;;;
(def list-a [1 2 3])
list-a

(def list-b (conj list-a 4))
list-a
list-b

(def list-c (vec (rest list-b)))
list-a
list-b
list-c

; immutable efficiency
(-> (load-image-resource "lists.png")
    show
    (.setAlwaysOnTop true))








;;;;;;;;;;;;;;;;;;;;
;; 6. atoms (cas) ;;
;;;;;;;;;;;;;;;;;;;;
(def list-atom (atom [1 2 3 4 5 6]))
list-atom
@list-atom

(swap! list-atom rest)
@list-atom
(def my-snapshot @list-atom)
my-snapshot

(-> (load-image-resource "epochal-time-model.png")
    show
    (.setAlwaysOnTop true))









; refs (stm)
(def my-account (ref 100))
(def your-account (ref 100))
@my-account
@your-account

(dosync
 (let [amt 50]
   (alter your-account - amt)
   (alter my-account + amt)
   amt))
@my-account
@your-account







;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 7. high-order functions ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def coll (range 10))

coll

(filter even? coll)

(defn doublify [x] (* 2 x))
(map doublify coll)

(reduce + (range 10))

(group-by
 count
 ["a" "as" "asd" "aa" "asdf" "qwer"])












;;;;;;;;;;;;;;;;;;;;;;;;;
;; 8. threading macros ;;
;;;;;;;;;;;;;;;;;;;;;;;;;
; thread first
(-> {}
    (assoc :foo "bar")
    (assoc :fuz "baz")
    keys
    count)

; thread last
(->> (range 100)
     (filter even?)
     (map doublify)
     (reduce +))









;;;;;;;;;;;;;;;;;;;;
;; 9. concurrency ;;
;;;;;;;;;;;;;;;;;;;;
(defn stupid-doublify [x]
  (Thread/sleep 100)
  (* 2 x))

(map stupid-doublify (range 50))
; pmap
(pmap stupid-doublify (range 50))









; future
(def thing (atom nil))
(future
  (Thread/sleep 5000)
  (reset! thing :foo))
@thing






; promise
(def promise-thing (promise))
(future
  (Thread/sleep 5000)
  (deliver promise-thing :bar))

@promise-thing









;;;;;;;;;;;;;;;;;;;;
;; 10. core.async ;;
;;;;;;;;;;;;;;;;;;;;
(require ['clojure.core.async :as 'async
          :refer '[go go-loop chan close! timeout alts! put! <! <!! >! >!!]])

(<!!
 (go
  (<! (timeout 2000))
  (+ 1 1)))

; channel
(def channel (chan 10))

(go-loop
 [n 10]
 (if (> n 0)
   (do
     (<! (timeout 2000))
     (>! channel n)
     (recur (dec n)))
   (close! channel)))

(<!! channel)


(let [c1 (chan)
      c2 (chan)]
  (go (while true
        (let [[v ch] (alts! [c1 c2])]
          (println "Read" v "from" ch))))
  (put! c1 "hi")
  (put! c2 "there"))






;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 10. just the good bits from OOP ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; multi-arity
(defn greeting
  ([] (greeting "Stranger" 1))
  ([name] (greeting name 1))
  ([name times]
   (clojure.string/join ", " (repeat times (str "Hello " name)))))
(greeting)
(greeting "Sue")
(greeting "Sue" 4)



(macroexpand '(go (println "hey")))





; multi-method
(defmulti silly-math (fn [x]
                       (cond
                        (odd? x) :odd
                        (> x 10) :big
                        (even? x) :small-even)))
(defmethod silly-math :odd [x] (inc x))
(defmethod silly-math :big [x] (* x x))
(defmethod silly-math :small-even [x] (+ (* 2 x) 3))
(silly-math 8)
(silly-math 17)
(silly-math 22)








; protocols
(defprotocol Shape
  (area [s])
  (perimiter [s]))

(defrecord Rectangle [w h]
  Shape
  (area [this] (* w h))
  (perimiter [this] (+ (* 2 w)
                       (* 2 h))))

(defrecord Circle [r]
  Shape
  (area [this] (* Math/PI r r))
  (perimiter [this] (* 2 Math/PI r)))

(def rect1 (->Rectangle 10 12))
(def circ1 (->Circle 6))

(area rect1)
(perimiter rect1)

(area circ1)
(perimiter circ1)






(use 'clojure.string)

(extend java.lang.String
  Shape
  {:area (fn [this]
           )})








;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 11. namespaces and general organization ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

