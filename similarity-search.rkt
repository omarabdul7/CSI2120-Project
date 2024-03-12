#lang scheme

; Loads histogram from a file
(define (loadHistogram filePath)
  (with-input-from-file filePath
    (lambda ()
      (let ((skip (read-line))
            (data (read-line)))
        (if data
            (map string->number (string-split data " "))
            '())))))

; Trims last element from a list
(define (trimLast list)
  (if (null? (cdr list))
      '()
      (cons (car list) (trimLast (cdr list)))))

; Parses histogram from data file
(define (parseData file)
  (with-input-from-file file
    (lambda ()
      (read-line)
      (let ((data (read-line)))
        (if data
            (let ((histogram (map string->number (regexp-split #rx" " data))))
              (if (not (null? histogram))
                  (cons (extractName file)
                        (trimLast histogram))
                  '()))
            '())))))

; Extracts filename from path
(define (extractName path)
  (list-ref (reverse (regexp-split #rx"/" path)) 0))

; Processes histograms in a directory
(define (processHistograms path)
  (filter (lambda (item) (not (null? (cdr item))))
          (map (lambda (file)
                 (parseData (build-path path file)))
               (directory-list path))))

; Calculates histogram similarity
(define (similarity hist1 hist2)
  (let ((sumMins (apply + (map min hist1 hist2)))
        (sumHist1 (apply + hist1)))
    (/ sumMins sumHist1)))

; Finds similar images, omits similarity scores in output
(define (findSimilar queryHist path)
  (define hists (processHistograms path))
  (define scores
    (map (lambda (entry)
           (let ((name (car entry))
                 (hist (cdr entry)))
             (cons name (similarity queryHist hist))))
         hists))
  (define sorted
    (sort scores
          (lambda (a b) (> (cdr a) (cdr b)))))
  (printf "Top 5 matching images:\n")
  (for-each (lambda (entry)
              (printf "~a\n" (car entry)))
            (take sorted 5)))

; Setup and execute
(define queryHist (loadHistogram "Qhistograms/q03.txt"))
(define path "Dhistogram")
(findSimilar queryHist path)
