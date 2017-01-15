(ns depict.core
  (:gen-class)
  (:import [org.openscience.cdk Atom Bond AminoAcid AtomContainer])
  (:import [org.openscience.cdk.depict Depiction DepictionGenerator MolGridDepiction])
  (:import [org.openscience.cdk.interfaces IAtomContainer])
  (:import [org.openscience.cdk.io MDLV2000Reader])
  (:import [org.openscience.cdk.layout StructureDiagramGenerator])
  (:import [org.openscience.cdk.renderer.color UniColor])
  (:import [org.openscience.cdk.silent SilentChemObjectBuilder])
  (:import [org.openscience.cdk.smiles SmilesParser])
  (:import [org.openscience.cdk.smiles.smarts SmartsPattern])
  (:import [java.io File FileReader])
  (:import [java.awt Color Rectangle])
  (:import [java.awt.image BufferedImage]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Selection-Related

(defn create-smarts-pattern
  "create a SMARTS Pattern form a string"
  (^SmartsPattern [^String s]  (SmartsPattern/create s nil)))

(defn match-smarts [^String s ^AtomContainer mol limit]
  "find matching smarts strings in a molecule. return a vector of Atoms and Bonds"

  (let [pattern (create-smarts-pattern s)
        hitset  (-> pattern
                    (.matchAll mol)
                    (.limit limit)
                    (.uniqueAtoms)
                    (.toAtomBondMap))]
    (flatten
      (for [s hitset]
        (for [s1 (.entrySet s)]
          (.getValue s1))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; IO-Related

(defn read-mol
  "read a molfile and return an AtomContainer"
  (^AtomContainer [filename]
   (let [file (File. filename)
         freader (FileReader. file)
         mdlreader (MDLV2000Reader. freader)]
     (.read mdlreader (AtomContainer.)))))


(defn parse-smiles
  "read in a smiles string"
  (^IAtomContainer [smi]
   (let [builder (SilentChemObjectBuilder/getInstance)
         sp    (SmilesParser.  builder)
         mol   (.parseSmiles sp smi)
         sdg   (doto (StructureDiagramGenerator.)
                 (.setMolecule mol)
                 (.generateCoordinates))]
     (.getMolecule sdg))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Depiction Generation Functions

(defn depiction
  "create a DepictionGenerator"
  (^DepictionGenerator [](DepictionGenerator.)))

(defn add-outerglow
  "apply outerglow highlighting"
  (^DepictionGenerator [^DepictionGenerator dg] (.withOuterGlowHighlight dg)))

(defn add-title
  "add-title"
  (^DepictionGenerator [^DepictionGenerator dg]
   (.withMolTitle dg)))

(defn add-terminal-carbons
  "add-terminal-carbons"
  (^DepictionGenerator [^DepictionGenerator dg]
   (.withTerminalCarbons dg)))

(defn color-atoms
  "color the depiction"
  (^DepictionGenerator [^DepictionGenerator dg] (.withAtomColors dg)))

(defn highlight-atoms
  "highlight a list of chemobjects (atoms/bonds) in a Depiction"
  (^DepictionGenerator [^DepictionGenerator dg chemobjs]
   (.withHighlight dg chemobjs))
  (^DepictionGenerator [^DepictionGenerator dg chemobjs ^Color color]
   (.withHighlight dg chemobjs color)))

(defn set_size
  "set the size of the depiciton generator"
  (^DepictionGenerator [^DepictionGenerator dg] dg)
  (^DepictionGenerator [^DepictionGenerator dg ^int w ^ int h]
   (.withSize dg w h)))

(defn set-zoom
  "set the zoom"
  (^DepictionGenerator [^DepictionGenerator dg ^float zoom]
   (.withZoom dg zoom)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Depict

(defn depict
  "depict a molecule"
  (^MolGridDepiction [^DepictionGenerator dg ^IAtomContainer mol]
    (.depict dg mol)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Image output Functions


(defn save-image
  "write an image to a file"
  ([^MolGridDepiction mgd outfile ] (.writeTo mgd outfile)))

(defn get-image
  "depict a molecule"
  (^java.awt.image.BufferedImage [^MolGridDepiction mdg] (.toImg mdg)))

(defn get-vector-string
  "depict a molecule"
  (^String [^MolGridDepiction mgd] (.toSvgStr mgd)))



(comment

  (defn draw-mol
    "draw a molecule"
    [mol smarts color backgroundcol outfile]
    (let [allbonds (.bonds mol)
          allatoms (.atoms mol)
          highlight (match-smarts smarts mol 3)
          dg ( -> (DepictionGenerator.)
                  (.withSize 600 600)
                  (.withAtomColors (UniColor. Color/BLACK))
                  (.withHighlight allbonds backgroundcol) ; colorF is grey
                  (.withHighlight allatoms backgroundcol)
                  (.withHighlight highlight color)
                  (.withOuterGlowHighlight))
          dep (.depict dg mol)]
      (.writeTo dep outfile)))


  (def colorA (Color. 169, 199, 255))
  (def colorB (Color. 185, 255, 180))
  (def colorC (Color. 255, 162, 162))
  (def colorD (Color. 253, 139, 255))
  (def colorE (Color. 255, 206, 86))
  (def colorF (Color. 227, 227, 227)))
