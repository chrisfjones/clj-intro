(use 'mikera.image.core)
(-> (load-image-resource "logo.png")
    show
    (.setAlwaysOnTop true)); Pre
; 1. I've been working with clojure for a few years on small/medium size projects
; 2. I'll draw a lot of comparisons with Java, but I don't mean to put it down
; 3. "Clojure asks you to step up your game, and rewards you for doing so" - Stuart Halloway
; 4. I struggled a bit with the flow of this material. There are several concepts that all fit together to form the whole, but there is no clear sequence.

;;;;;;;;;;;;;;;;;;;;
;; 1. Live coding ;;
;;;;;;;;;;;;;;;;;;;;
(+ 1 1)










;;;;;;;;;;;;;;;;;;;;;
;; 2. Java interop ;;
;;;;;;;;;;;;;;;;;;;;;
(System/currentTimeMillis)
(get (System/getProperties) "java.version")
(new java.util.Date)

(org.apache.commons.math3.util.ArithmeticUtils/addAndCheck 1 1)







;;;;;;;;;;;;;;;;;;;;;
;; 3. Data as Code ;;
;;;;;;;;;;;;;;;;;;;;;

; vector
[1 2 3]

; map
{:key1 "foo" :key2 "bar"}

; set
#{:a :b :c}









;;;;;;;;;;;;;;;;;;;;;
;; 4. Code as Data ;;
;;;;;;;;;;;;;;;;;;;;;
(def my-add-fn (fn [x y] (+ x y)))

(my-add-fn 1 2)

((identity my-add-fn) 1 1)


; eval
(eval '(+ 1 1))

; macros
(defmacro postfix-notation [expression]
  expression
  (conj (butlast expression) (last expression)))

(postfix-notation
 (1 2 +))

(macroexpand '(postfix-notation
               (1 2 +)))

(with-open [in-stream (clojure.java.io/input-stream "resources/textfile.txt")]
  (.available in-stream))

(println "holy shit") ; todo: maybe picture of exploding head here








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
(defn doublify [x] (* 2 x))

(map doublify coll)

(filter even? coll)

(reduce + (range 10))





;;;;;;;;;;;;;;;;;;;;;;;;;
;; 8. threading macros ;;
;;;;;;;;;;;;;;;;;;;;;;;;;









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









;;;;;;;;;;;;;;;;;;;;
;; 10. core.async ;;
;;;;;;;;;;;;;;;;;;;;
(use 'clojure.core.async)

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
     (<! (timeout 1000))
     (>! channel n)
     (recur (dec n)))
   (close! channel)))

(<!! channel)










;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 10. namespaces and general organization ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;












;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 11. just the good bits from OOP ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;   - multi-method
;   - protocols (show power by adding a method to String)
;   - defrecord
