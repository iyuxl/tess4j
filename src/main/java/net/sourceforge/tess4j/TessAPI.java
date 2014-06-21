/**
 * Copyright @ 2014 Quan Nguyen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.sourceforge.tess4j;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * A Java wrapper for <code>Tesseract OCR 3.02 API</code> using <code>JNA Interface Mapping</code>.
 */
public interface TessAPI extends Library {

    static final boolean        WINDOWS          = System.getProperty("os.name").toLowerCase().startsWith("windows");
    /**
     * Native library name.
     */
    public static final String  LIB_NAME         = "libtesseract302";
    public static final String  LIB_NAME_NON_WIN = "tesseract";
    /**
     * An instance of the class library.
     */
    public static final TessAPI INSTANCE         = (TessAPI) Native.loadLibrary(WINDOWS ? LIB_NAME : LIB_NAME_NON_WIN,
                                                         TessAPI.class);

    /**
     * When Tesseract/Cube is initialized we can choose to instantiate/load/run only the Tesseract part, only the Cube
     * part or both along with the combiner. The preference of which engine to use is stored in
     * <code>tessedit_ocr_engine_mode</code>.<br>
     * <br>
     * ATTENTION: When modifying this enum, please make sure to make the appropriate changes to all the enums mirroring
     * it (e.g. OCREngine in cityblock/workflow/detection/detection_storage.proto). Such enums will mention the
     * connection to OcrEngineMode in the comments.
     */
    public static interface TessOcrEngineMode {

        /**
         * Run Tesseract only - fastest
         */
        public static final int OEM_TESSERACT_ONLY          = 0;
        /**
         * Run Cube only - better accuracy, but slower
         */
        public static final int OEM_CUBE_ONLY               = 1;
        /**
         * Run both and combine results - best accuracy
         */
        public static final int OEM_TESSERACT_CUBE_COMBINED = 2;
        /**
         * Specify this mode when calling init_*(), to indicate that any of the above modes should be automatically
         * inferred from the variables in the language-specific config, command-line configs, or if not specified in any
         * of the above should be set to the default OEM_TESSERACT_ONLY.
         */
        public static final int OEM_DEFAULT                 = 3;
    };

    /**
     * Possible modes for page layout analysis. These *must* be kept in order of decreasing amount of layout analysis to
     * be done, except for <code>OSD_ONLY</code>, so that the inequality test macros below work.
     */
    public static interface TessPageSegMode {

        /**
         * Orientation and script detection only.
         */
        public static final int PSM_OSD_ONLY               = 0;
        /**
         * Automatic page segmentation with orientation and script detection. (OSD)
         */
        public static final int PSM_AUTO_OSD               = 1;
        /**
         * Automatic page segmentation, but no OSD, or OCR.
         */
        public static final int PSM_AUTO_ONLY              = 2;
        /**
         * Fully automatic page segmentation, but no OSD.
         */
        public static final int PSM_AUTO                   = 3;
        /**
         * Assume a single column of text of variable sizes.
         */
        public static final int PSM_SINGLE_COLUMN          = 4;
        /**
         * Assume a single uniform block of vertically aligned text.
         */
        public static final int PSM_SINGLE_BLOCK_VERT_TEXT = 5;
        /**
         * Assume a single uniform block of text.
         */
        public static final int PSM_SINGLE_BLOCK           = 6;
        /**
         * Treat the image as a single text line.
         */
        public static final int PSM_SINGLE_LINE            = 7;
        /**
         * Treat the image as a single word.
         */
        public static final int PSM_SINGLE_WORD            = 8;
        /**
         * Treat the image as a single word in a circle.
         */
        public static final int PSM_CIRCLE_WORD            = 9;
        /**
         * Treat the image as a single character.
         */
        public static final int PSM_SINGLE_CHAR            = 10;
        /**
         * Find as much text as possible in no particular order.
         */
        public static final int PSM_SPARSE_TEXT            = 11;
        /**
         * Sparse text with orientation and script detection.
         */
        public static final int PSM_SPARSE_TEXT_OSD        = 12;
        /**
         * Number of enum entries.
         */
        public static final int PSM_COUNT                  = 13;
    };

    /**
     * Enum of the elements of the page hierarchy, used in <code>ResultIterator</code> to provide functions that operate
     * on each level without having to have 5x as many functions.
     */
    public static interface TessPageIteratorLevel {

        /**
         * Block of text/image/separator line.
         */
        public static final int RIL_BLOCK    = 0;
        /**
         * Paragraph within a block.
         */
        public static final int RIL_PARA     = 1;
        /**
         * Line within a paragraph.
         */
        public static final int RIL_TEXTLINE = 2;
        /**
         * Word within a textline.
         */
        public static final int RIL_WORD     = 3;
        /**
         * Symbol/character within a word.
         */
        public static final int RIL_SYMBOL   = 4;
    };

    public static interface TessPolyBlockType {

        /**
         * Type is not yet known. Keep as the first element.
         */
        public static final int PT_UNKNOWN         = 0;
        /**
         * Text that lives inside a column.
         */
        public static final int PT_FLOWING_TEXT    = 1;
        /**
         * Text that spans more than one column.
         */
        public static final int PT_HEADING_TEXT    = 2;
        /**
         * Text that is in a cross-column pull-out region.
         */
        public static final int PT_PULLOUT_TEXT    = 3;
        /**
         * Partition belonging to an equation region.
         */
        public static final int PT_EQUATION        = 4;
        /**
         * Partition has inline equation.
         */
        public static final int PT_INLINE_EQUATION = 5;
        /**
         * Partition belonging to a table region.
         */
        public static final int PT_TABLE           = 6;
        /**
         * Text-line runs vertically.
         */
        public static final int PT_VERTICAL_TEXT   = 7;
        /**
         * Text that belongs to an image.
         */
        public static final int PT_CAPTION_TEXT    = 8;
        /**
         * Image that lives inside a column.
         */
        public static final int PT_FLOWING_IMAGE   = 9;
        /**
         * Image that spans more than one column.
         */
        public static final int PT_HEADING_IMAGE   = 10;
        /**
         * Image that is in a cross-column pull-out region.
         */
        public static final int PT_PULLOUT_IMAGE   = 11;
        /**
         * Horizontal Line.
         */
        public static final int PT_HORZ_LINE       = 12;
        /**
         * Vertical Line.
         */
        public static final int PT_VERT_LINE       = 13;
        /**
         * Lies outside of any column.
         */
        public static final int PT_NOISE           = 14;
        /**
         * Number of enum entries.
         */
        public static final int PT_COUNT           = 15;
    };

/**
     * <pre>
     *  +------------------+
     *  | 1 Aaaa Aaaa Aaaa |
     *  | Aaa aa aaa aa    |
     *  | aaaaaa A aa aaa. |
     *  |                2 |
     *  |   #######  c c C |
     *  |   #######  c c c |
     *  | < #######  c c c |
     *  | < #######  c   c |
     *  | < #######  .   c |
     *  | 3 #######      c |
     *  +------------------+
     * </pre> Orientation Example:
     * <br>
     * ====================
     * <br>
     * Above is a diagram of some (1) English and (2) Chinese text and a (3)
     * photo credit.<br>
     * <br>
     * Upright Latin characters are represented as A and a. '<' represents a
     * latin character rotated anti-clockwise 90 degrees. Upright Chinese
     * characters are represented C and c.<br> <br> NOTA BENE: enum values
     * here should match goodoc.proto<br> <br> If you orient your head so that
     * "up" aligns with Orientation, then the characters will appear "right side
     * up" and readable.<br>
     * <br>
     * In the example above, both the English and Chinese paragraphs are
     * oriented so their "up" is the top of the page (page up). The photo credit
     * is read with one's head turned leftward ("up" is to page left).<br>
     * <br> The values of this enum match the convention of Tesseract's
     * osdetect.h
     */
    public static interface TessOrientation {

        public static final int ORIENTATION_PAGE_UP    = 0;
        public static final int ORIENTATION_PAGE_RIGHT = 1;
        public static final int ORIENTATION_PAGE_DOWN  = 2;
        public static final int ORIENTATION_PAGE_LEFT  = 3;
    };

    /**
     * The grapheme clusters within a line of text are laid out logically in this direction, judged when looking at the
     * text line rotated so that its Orientation is "page up".<br>
     * <br>
     * For English text, the writing direction is left-to-right. For the Chinese text in the above example, the writing
     * direction is top-to-bottom.
     */
    public static interface TessWritingDirection {

        public static final int WRITING_DIRECTION_LEFT_TO_RIGHT = 0;
        public static final int WRITING_DIRECTION_RIGHT_TO_LEFT = 1;
        public static final int WRITING_DIRECTION_TOP_TO_BOTTOM = 2;
    };

    /**
     * The text lines are read in the given sequence.<br>
     * <br>
     * In English, the order is top-to-bottom. In Chinese, vertical text lines are read right-to-left. Mongolian is
     * written in vertical columns top to bottom like Chinese, but the lines order left-to right.<br>
     * <br>
     * Note that only some combinations make sense. For example, <code>WRITING_DIRECTION_LEFT_TO_RIGHT</code> implies
     * <code>TEXTLINE_ORDER_TOP_TO_BOTTOM</code>.
     */
    public static interface TessTextlineOrder {

        public static final int TEXTLINE_ORDER_LEFT_TO_RIGHT = 0;
        public static final int TEXTLINE_ORDER_RIGHT_TO_LEFT = 1;
        public static final int TEXTLINE_ORDER_TOP_TO_BOTTOM = 2;
    };

    public static final int TRUE  = 1;
    public static final int FALSE = 0;

    /**
     * @return the version identifier.
     */
    String TessVersion();

    /**
     * @param text used to delete text
     */
    void TessDeleteText(Pointer text);

    /**
     * @param arr text array pointer reference
     */
    void TessDeleteTextArray(PointerByReference arr);

    /**
     * @param arr int buffer array to be deleted
     */
    void TessDeleteIntArray(IntBuffer arr);

    /**
     * @return an instance of the base class for all Tesseract APIs.
     */
    TessAPI.TessBaseAPI TessBaseAPICreate();

    /**
     * Disposes the TesseractAPI instance.
     * 
     * @param handle the TesseractAPI instance
     */
    void TessBaseAPIDelete(TessAPI.TessBaseAPI handle);

    /**
     * Set the name of the input file. Needed only for training and reading a UNLV zone file.
     * 
     * @param handle the TesseractAPI instance
     * @param name pass the name to be set
     */
    void TessBaseAPISetInputName(TessAPI.TessBaseAPI handle, String name);

    /**
     * Set the name of the bonus output files. Needed only for debugging.
     * 
     * @param handle the TesseractAPI instance
     * @param name pass the name to be set
     */
    void TessBaseAPISetOutputName(TessAPI.TessBaseAPI handle, String name);

    /**
     * Set the value of an internal "parameter." Supply the name of the parameter and the value as a string, just as you
     * would in a config file. Returns false if the name lookup failed.<br>
     * E.g.: <code>SetVariable("tessedit_char_blacklist", "xyz");</code> to ignore x, y and z. Or
     * <code>SetVariable("classify_bln_numeric_mode", "1");</code> to set numeric-only mode. <code>SetVariable</code>
     * may be used before <code>Init</code>, but settings will revert to defaults on <code>End()</code>.<br>
     * 
     * <pre>
     * Note: Must be called after <code>Init()</code>. Only works for non-init variables (init variables should be
     * passed to <code>Init()</code>).
     * </pre>
     * 
     * @param handle the TesseractAPI instance
     * @param name name of the input
     * @param value variable value
     * @return 1 is true and (0) is false
     */
    int TessBaseAPISetVariable(TessAPI.TessBaseAPI handle, String name, String value);

    /**
     * Fills in value with the value of the parameter.
     * 
     * @param handle the TesseractAPI instance
     * @param name name of the input
     * @param value pass the int buffer value
     * @return 1 is true and (0) is false
     */
    int TessBaseAPIGetIntVariable(TessAPI.TessBaseAPI handle, String name, IntBuffer value);

    /**
     * Fills in value with the value of the parameter.
     * 
     * @param handle the TesseractAPI instance
     * @param name pass the name of the variable
     * @param value pass the int buffer value
     * @return 1 (true) if the parameter was found among Tesseract parameters
     */
    int TessBaseAPIGetBoolVariable(TessAPI.TessBaseAPI handle, String name, IntBuffer value);

    /**
     * @param handle the TesseractAPI instance
     * @param name pass the name of the variable
     * @param value pass the double buffer value
     * @return double value
     */
    int TessBaseAPIGetDoubleVariable(TessAPI.TessBaseAPI handle, String name, DoubleBuffer value);

    /**
     * @param handle the TesseractAPI instance
     * @param name pass the name of the variable
     * @return the string variable
     */
    String TessBaseAPIGetStringVariable(TessAPI.TessBaseAPI handle, String name);

    /**
     * Print Tesseract parameters to the given file.<br>
     * <br>
     * Note: Must not be the first method called after instance create.
     * 
     * @param handle the TesseractAPI instance
     * @param filename name of the file where the variables will be persisted
     */
    void TessBaseAPIPrintVariablesToFile(TessAPI.TessBaseAPI handle, String filename);

    /**
     * Instances are now mostly thread-safe and totally independent, but some global parameters remain. Basically it is
     * safe to use multiple TessBaseAPIs in different threads in parallel.<br>
     * UNLESS:<br>
     * you use <code>SetVariable</code> on some of the Params in classify and textord. If you do, then the effect will
     * be to change it for all your instances.<br>
     * <br>
     * Start tesseract. Returns zero on success and -1 on failure. NOTE that the only members that may be called before
     * Init are those listed above here in the class definition.<br>
     * <br>
     * It is entirely safe (and eventually will be efficient too) to call Init multiple times on the same instance to
     * change language, or just to reset the classifier. Languages may specify internally that they want to be loaded
     * with one or more other languages, so the <i>~</i> sign is available to override that. E.g., if hin were set to
     * load eng by default, then hin+~eng would force loading only hin. The number of loaded languages is limited only
     * by memory, with the caveat that loading additional languages will impact both speed and accuracy, as there is
     * more work to do to decide on the applicable language, and there is more chance of hallucinating incorrect words.
     * 
     * <pre>
     * WARNING:<br>
     * On changing languages, all Tesseract parameters are reset back to their default values. (Which may vary between languages.) If you have a
     * rare need to set a Variable that controls initialization for a second call to <code>Init</code> you should
     * explicitly call <code>End()</code> and then use <code>SetVariable</code> before <code>Init</code>.<br>
     * This is only a very rare use case, since there are very few uses that require any parameters to be set before <code>Init</code>.<br>
     * <br>
     * If <code>set_only_non_debug_params</code> is true, only params that do not contain "debug" in the name will be
     * set.
     * </pre>
     * 
     * @param handle the TesseractAPI instance
     * @param datapath The <code>datapath</code> must be the name of the parent directory of tessdata and must end in
     * <i>/</i>. Any name after the last <i>/</i> will be stripped.
     * @param language The language is (usually) an <code>ISO 639-3</code> string or <code>NULL</code> will default to
     * eng. The language may be a string of the form [~]<lang>[+[~]<lang>] indicating that multiple languages are to be
     * loaded. E.g., hin+eng will load Hindi and English.
     * @param oem ocr engine mode
     * @param configs pointer configuration
     * @param configs_size pointer configuration size
     * @return integer configuration size
     */
    int TessBaseAPIInit1(TessAPI.TessBaseAPI handle, String datapath, String language, int oem,
            PointerByReference configs, int configs_size);

    /**
     * @param handle the TesseractAPI instance
     * @param datapath The <code>datapath</code> must be the name of the parent directory of tessdata and must end in
     * <i>/</i>. Any name after the last <i>/</i> will be stripped.
     * @param language The language is (usually) an <code>ISO 639-3</code> string or <code>NULL</code> will default to
     * eng. The language may be a string of the form [~]<lang>[+[~]<lang>] indicating that multiple languages are to be
     * loaded. E.g., hin+eng will load Hindi and English.
     * @param oem ocr engine mode
     * @return tesseract api init2 value
     */
    int TessBaseAPIInit2(TessAPI.TessBaseAPI handle, String datapath, String language, int oem);

    /**
     * @param handle the TesseractAPI instance
     * @param datapath The <code>datapath</code> must be the name of the parent directory of tessdata and must end in
     * <i>/</i>. Any name after the last <i>/</i> will be stripped.
     * @param language The language is (usually) an <code>ISO 639-3</code> string or <code>NULL</code> will default to
     * eng. The language may be a string of the form [~]<lang>[+[~]<lang>] indicating that multiple languages are to be
     * loaded. E.g., hin+eng will load Hindi and English.
     * @return tesseract api init3 value
     */
    int TessBaseAPIInit3(TessAPI.TessBaseAPI handle, String datapath, String language);

    /**
     * Returns the languages string used in the last valid initialization. If the last initialization specified
     * "deu+hin" then that will be returned. If hin loaded eng automatically as well, then that will not be included in
     * this list. To find the languages actually loaded, use <code>GetLoadedLanguagesAsVector</code>. The returned
     * string should NOT be deleted.
     * 
     * @param handle the TesseractAPI instance
     * @return languages as string
     */
    String TessBaseAPIGetInitLanguagesAsString(TessAPI.TessBaseAPI handle);

    /**
     * Returns the loaded languages in the vector of STRINGs. Includes all languages loaded by the last
     * <code>Init</code>, including those loaded as dependencies of other loaded languages.
     * 
     * @param handle the TesseractAPI instance
     * @return loaded languages as vector
     */
    PointerByReference TessBaseAPIGetLoadedLanguagesAsVector(TessAPI.TessBaseAPI handle);

    /**
     * Returns the available languages in the vector of STRINGs.
     * 
     * @param handle the TesseractAPI instance
     * @return available languages as vector
     */
    PointerByReference TessBaseAPIGetAvailableLanguagesAsVector(TessAPI.TessBaseAPI handle);

    /**
     * Init only the lang model component of Tesseract. The only functions that work after this init are
     * <code>SetVariable</code> and <code>IsValidWord</code>. WARNING: temporary! This function will be removed from
     * here and placed in a separate API at some future time.
     * 
     * @param handle the TesseractAPI instance
     * @param datapath The <code>datapath</code> must be the name of the parent directory of tessdata and must end in
     * <i>/</i>. Any name after the last <i>/</i> will be stripped.
     * @param language The language is (usually) an <code>ISO 639-3</code> string or <code>NULL</code> will default to
     * eng. The language may be a string of the form [~]<lang>[+[~]<lang>] indicating that multiple languages are to be
     * loaded. E.g., hin+eng will load Hindi and English.
     * @return api init language mode
     */
    int TessBaseAPIInitLangMod(TessAPI.TessBaseAPI handle, String datapath, String language);

    /**
     * Init only for page layout analysis. Use only for calls to <code>SetImage</code> and <code>AnalysePage</code>.
     * Calls that attempt recognition will generate an error.
     * 
     * @param handle the TesseractAPI instance
     */
    void TessBaseAPIInitForAnalysePage(TessAPI.TessBaseAPI handle);

    /**
     * Read a "config" file containing a set of param, value pairs. Searches the standard places:
     * <code>tessdata/configs</code>, <code>tessdata/tessconfigs</code> and also accepts a relative or absolute path
     * name.
     * 
     * <pre>
     * Note:<br>
     * only non-init params will be set (init params are set by <code>Init()</code>).
     * </pre>
     * 
     * @param handle the TesseractAPI instance
     * @param filename relative or absolute path for the "config" file containing a set of param and value pairs.
     * @param init_only
     */
    void TessBaseAPIReadConfigFile(TessAPI.TessBaseAPI handle, String filename, int init_only);

    /**
     * Set the current page segmentation mode. Defaults to PSM_SINGLE_BLOCK. The mode is stored as an IntParam so it can
     * also be modified by <code>ReadConfigFile</code> or
     * <code>SetVariable("tessedit_pageseg_mode", mode as string)</code>.
     * 
     * @param handle the TesseractAPI instance
     * @param mode tesseract page segment mode
     */
    void TessBaseAPISetPageSegMode(TessAPI.TessBaseAPI handle, int mode);

    /**
     * Return the current page segmentation mode.
     * 
     * @param handle the TesseractAPI instance
     * @return page segment mode value
     */
    int TessBaseAPIGetPageSegMode(TessAPI.TessBaseAPI handle);

    /**
     * Recognize a rectangle from an image and return the result as a string. May be called many times for a single
     * <code>Init</code>. Currently has no error checking. Greyscale of 8 and color of 24 or 32 bits per pixel may be
     * given. Palette color images will not work properly and must be converted to 24 bit. Binary images of 1 bit per
     * pixel may also be given but they must be byte packed with the MSB of the first byte being the first pixel, and a
     * 1 represents WHITE. For binary images set bytes_per_pixel=0. The recognized text is returned as a char* which is
     * coded as UTF8 and must be freed with the delete [] operator.<br>
     * <br>
     * Note that <code>TesseractRect</code> is the simplified convenience interface. For advanced uses, use
     * <code>SetImage</code>, (optionally) <code>SetRectangle</code>, <code>Recognize</code>, and one or more of the
     * <code>Get*Text</code> functions below.
     * 
     * @param handle the TesseractAPI instance
     * @param imagedata image byte buffer
     * @param bytes_per_pixel
     * @param bytes_per_line
     * @param left
     * @param top
     * @param width
     * @param height
     * @return api rectangle pointer object
     */
    Pointer TessBaseAPIRect(TessAPI.TessBaseAPI handle, ByteBuffer imagedata, int bytes_per_pixel, int bytes_per_line,
            int left, int top, int width, int height);

    /**
     * Call between pages or documents etc to free up memory and forget adaptive data.
     * 
     * @param handle the TesseractAPI instance
     */
    void TessBaseAPIClearAdaptiveClassifier(TessAPI.TessBaseAPI handle);

    /**
     * Provide an image for Tesseract to recognize. Format is as TesseractRect above. Does not copy the image buffer, or
     * take ownership. The source image may be destroyed after Recognize is called, either explicitly or implicitly via
     * one of the <code>Get*Text</code> functions. <code>SetImage</code> clears all recognition results, and sets the
     * rectangle to the full image, so it may be followed immediately by a <code>GetUTF8Text</code>, and it will
     * automatically perform recognition.
     * 
     * @param handle the TesseractAPI instance
     * @param imagedata image byte buffer
     * @param width image width
     * @param height image height
     * @param bytes_per_pixel bytes per pixel
     * @param bytes_per_line bytes per line
     */
    void TessBaseAPISetImage(TessAPI.TessBaseAPI handle, ByteBuffer imagedata, int width, int height,
            int bytes_per_pixel, int bytes_per_line);

    /**
     * Set the resolution of the source image in pixels per inch so font size information can be calculated in results.
     * Call this after SetImage().
     * 
     * @param handle the TesseractAPI instance
     * @param ppi source resolution value
     */
    void TessBaseAPISetSourceResolution(TessAPI.TessBaseAPI handle, int ppi);

    /**
     * Restrict recognition to a sub-rectangle of the image. Call after <code>SetImage</code>. Each
     * <code>SetRectangle</code> clears the recognition results so multiple rectangles can be recognized with the same
     * image.
     * 
     * @param handle the TesseractAPI instance
     * @param left value
     * @param top value
     * @param width value
     * @param height value
     */
    void TessBaseAPISetRectangle(TessAPI.TessBaseAPI handle, int left, int top, int width, int height);

    /**
     * @param handle the TesseractAPI instance
     * @return Scale factor from original image.
     */
    int TessBaseAPIGetThresholdedImageScaleFactor(TessAPI.TessBaseAPI handle);

    /**
     * Dump the internal binary image to a PGM file.
     * 
     * @param handle the TesseractAPI instance
     * @param filename pgm file name
     */
    void TessBaseAPIDumpPGM(TessAPI.TessBaseAPI handle, String filename);

    /**
     * Runs page layout analysis in the mode set by SetPageSegMode. May optionally be called prior to Recognize to get
     * access to just the page layout results. Returns an iterator to the results. Returns NULL on error. The returned
     * iterator must be deleted after use.<br>
     * 
     * <pre>
     * WARNING!<br>
     * This class points to data held within the TessBaseAPI class, and
     * therefore can only be used while the TessBaseAPI class still exists and has not been subjected to a call of
     * <code>Init</code>, <code>SetImage</code>, <code>Recognize</code>, <code>Clear</code>, <code>End</code>, DetectOS,
     * or anything else that changes the internal PAGE_RES.
     * </pr>
     * 
     * @param handle the TesseractAPI instance
     * @return returns an iterator to the results. Returns NULL on error. The returned
     * iterator must be deleted after use.
     */
    TessAPI.TessPageIterator TessBaseAPIAnalyseLayout(TessAPI.TessBaseAPI handle);

    /**
     * Recognize the image from SetAndThresholdImage, generating Tesseract internal structures. Returns 0 on success.
     * Optional. The <code>Get*Text</code> functions below will call <code>Recognize</code> if needed. After Recognize,
     * the output is kept internally until the next <code>SetImage</code>.
     * 
     * @param handle the TesseractAPI instance
     * @param monitor eanycode char representing the monitor status
     * @return 0 on success
     */
    int TessBaseAPIRecognize(TessAPI.TessBaseAPI handle, TessAPI.ETEXT_DESC monitor);

    /**
     * Variant on Recognize used for testing chopper.
     * 
     * @param handle the TesseractAPI instance
     * @param monitor eanycode char representing the monitor status
     * @return api status
     */
    int TessBaseAPIRecognizeForChopTest(TessAPI.TessBaseAPI handle, TessAPI.ETEXT_DESC monitor);

    /**
     * Get a reading-order iterator to the results of LayoutAnalysis and/or Recognize. The returned iterator must be
     * deleted after use. WARNING! This class points to data held within the TessBaseAPI class, and therefore can only
     * be used while the TessBaseAPI class still exists and has not been subjected to a call of <code>Init</code>,
     * <code>SetImage</code>, <code>Recognize</code>, <code>Clear</code>, <code>End</code>, DetectOS, or anything else
     * that changes the internal PAGE_RES.
     * 
     * @param handle the TesseractAPI instance
     * @return api iterator
     */
    TessAPI.TessResultIterator TessBaseAPIGetIterator(TessAPI.TessBaseAPI handle);

    /**
     * Get a mutable iterator to the results of LayoutAnalysis and/or Recognize. The returned iterator must be deleted
     * after use. WARNING! This class points to data held within the TessBaseAPI class, and therefore can only be used
     * while the TessBaseAPI class still exists and has not been subjected to a call of Init, SetImage, Recognize,
     * Clear, End DetectOS, or anything else that changes the internal PAGE_RES.
     * 
     * @param handle the TesseractAPI instance
     * @return api mutable iterator
     */
    TessAPI.TessMutableIterator TessBaseAPIGetMutableIterator(TessAPI.TessBaseAPI handle);

    /**
     * Recognizes all the pages in the named file, as a multi-page tiff or list of filenames, or single image, and gets
     * the appropriate kind of text according to parameters: <code>tessedit_create_boxfile</code>,
     * <code>tessedit_make_boxes_from_boxes</code>, <code>tessedit_write_unlv</code>, <code>tessedit_create_hocr</code>.
     * Calls ProcessPage on each page in the input file, which may be a multi-page tiff, single-page other file format,
     * or a plain text list of images to read. If tessedit_page_number is non-negative, processing begins at that page
     * of a multi-page tiff file, or filelist. The text is returned in text_out. Returns false on error. If non-zero
     * timeout_millisec terminates processing after the timeout on a single page. If non-NULL and non-empty, and some
     * page fails for some reason, the page is reprocessed with the retry_config config file. Useful for interactively
     * debugging a bad page.
     * 
     * @param handle the TesseractAPI instance
     * @param filename multi-page tiff or list of filenames
     * @param retry_config retry config values
     * @param timeout_millisec timeout value
     * @return TessBaseAPIProcessPages pointer
     */
    Pointer TessBaseAPIProcessPages(TessAPI.TessBaseAPI handle, String filename, String retry_config,
            int timeout_millisec);

    /**
     * The recognized text is returned as a char* which is coded as UTF-8 and must be freed with the delete [] operator.
     * 
     * @param handle the TesseractAPI instance
     * @return TessBaseAPIGetUTF8Text pointer
     */
    Pointer TessBaseAPIGetUTF8Text(TessAPI.TessBaseAPI handle);

    /**
     * Make a HTML-formatted string with hOCR markup from the internal data structures. page_number is 0-based but will
     * appear in the output as 1-based.
     * 
     * @param handle the TesseractAPI instance
     * @param page_number page number
     * @return HTML-formatted string with hOCR markup from the internal data structures
     */
    Pointer TessBaseAPIGetHOCRText(TessAPI.TessBaseAPI handle, int page_number);

    /**
     * @param handle the TesseractAPI instance
     * @param page_number number of the page
     * @return The recognized text is returned as a char* which is coded in the same format as a box file used in
     * training. Returned string must be freed with the delete [] operator. Constructs coordinates in the original image
     * - not just the rectangle. page_number is a 0-based page index that will appear in the box file.
     */
    Pointer TessBaseAPIGetBoxText(TessAPI.TessBaseAPI handle, int page_number);

    /**
     * @param handle the TesseractAPI instance
     * @return The recognized text is returned as a char* which is coded as UNLV format Latin-1 with specific reject and
     * suspect codes and must be freed with the delete [] operator.
     */
    Pointer TessBaseAPIGetUNLVText(TessAPI.TessBaseAPI handle);

    /**
     * @param handle the TesseractAPI instance
     * @return Returns the (average) confidence value between 0 and 100.
     */
    int TessBaseAPIMeanTextConf(TessAPI.TessBaseAPI handle);

    /**
     * @param handle the TesseractAPI instance
     * @return Returns all word confidences (between 0 and 100) in an array, terminated by -1. The calling function must
     * delete [] after use. The number of confidences should correspond to the number of space-delimited words in
     * GetUTF8Text.
     */
    IntByReference TessBaseAPIAllWordConfidences(TessAPI.TessBaseAPI handle);

    /**
     * Applies the given word to the adaptive classifier if possible. The word must be SPACE-DELIMITED UTF-8 - l i k e t
     * h i s , so it can tell the boundaries of the graphemes. Assumes that SetImage/SetRectangle have been used to set
     * the image to the given word. The mode arg should be PSM_SINGLE_WORD or PSM_CIRCLE_WORD, as that will be used to
     * control layout analysis. The currently set PageSegMode is preserved.
     * 
     * @param handle the TesseractAPI instance
     * @param mode tesseract page segment mode
     * @param wordstr The word must be SPACE-DELIMITED UTF-8 - l i k e t h i s , so it can tell the boundaries of the
     * graphemes.
     * @return false if adaption was not possible for some reason.
     */
    int TessBaseAPIAdaptToWordStr(TessAPI.TessBaseAPI handle, int mode, String wordstr);

    /**
     * Free up recognition results and any stored image data, without actually freeing any recognition data that would
     * be time-consuming to reload. Afterwards, you must call <code>SetImage</code> or <code>TesseractRect</code> before
     * doing any <code>Recognize</code> or <code>Get*</code> operation.
     * 
     * @param handle the TesseractAPI instance
     */
    void TessBaseAPIClear(TessAPI.TessBaseAPI handle);

    /**
     * Close down tesseract and free up all memory. <code>End()</code> is equivalent to destructing and reconstructing
     * your TessBaseAPI. Once <code>End()</code> has been used, none of the other API functions may be used other than
     * <code>Init</code> and anything declared above it in the class definition.
     * 
     * @param handle the TesseractAPI instance
     */
    void TessBaseAPIEnd(TessAPI.TessBaseAPI handle);

    /**
     * Check whether a word is valid according to Tesseract's language model.
     * 
     * @param handle the TesseractAPI instance
     * @param word word value
     * @return 1 if the word is invalid, non-zero if valid. @warning temporary! This function will be removed from here
     * and placed in a separate API at some future time.
     */
    int TessBaseAPIIsValidWord(TessAPI.TessBaseAPI handle, String word);

    /**
     * @param handle the TesseractAPI instance
     * @param out_offset offset
     * @param out_slope slope
     * @return text direction as integer value
     */
    int TessBaseAPIGetTextDirection(TessAPI.TessBaseAPI handle, IntBuffer out_offset, FloatBuffer out_slope);

    /**
     * @param handle the TesseractAPI instance
     * @param unichar_id the unichar id
     * @return the string form of the specified unichar.
     */
    String TessBaseAPIGetUnichar(TessAPI.TessBaseAPI handle, int unichar_id);

    /**
     * Deletes page iterator.
     * 
     * @param handle the TessPageIterator instance
     */
    void TessPageIteratorDelete(TessAPI.TessPageIterator handle);

    /**
     * @param handle the TesseractAPI instance
     * @return tesseract page iterator copy
     */
    TessAPI.TessPageIterator TessPageIteratorCopy(TessAPI.TessPageIterator handle);

    /**
     * @param handle the TessPageIterator instance
     */
    void TessPageIteratorBegin(TessAPI.TessPageIterator handle);

    /**
     * @param handle the TessPageIterator instance
     * @param level tesseract page level
     * @return next iterator object
     */
    int TessPageIteratorNext(TessAPI.TessPageIterator handle, int level);

    /**
     * @param handle the TessPageIterator instance
     * @param level tesseract page level
     * @return 1 is true
     */
    int TessPageIteratorIsAtBeginningOf(TessAPI.TessPageIterator handle, int level);

    /**
     * @param handle the TessPageIterator instance
     * @param level tesseract page level
     * @param element page iterator level
     * @return 1 is true
     */
    int TessPageIteratorIsAtFinalElement(TessAPI.TessPageIterator handle, int level, int element);

    /**
     * @param handle the TessPageIterator instance
     * @param level tesseract page level
     * @param left int buffer position
     * @param top int buffer position
     * @param right int buffer position
     * @param bottom int buffer position
     * @return boundingbox value
     */
    int TessPageIteratorBoundingBox(TessAPI.TessPageIterator handle, int level, IntBuffer left, IntBuffer top,
            IntBuffer right, IntBuffer bottom);

    /**
     * @param handle the TessPageIterator instance
     * @return tesseract page iterator blocktype value
     */
    int TessPageIteratorBlockType(TessAPI.TessPageIterator handle);

    /**
     * @param handle the TessPageIterator instance
     * @param level tesseract page level
     * @param x1 int buffer position
     * @param y1 int buffer position
     * @param x2 int buffer position
     * @param y2 int buffer position
     * @return tesseract page iterator baseline value
     */
    int TessPageIteratorBaseline(TessAPI.TessPageIterator handle, int level, IntBuffer x1, IntBuffer y1, IntBuffer x2,
            IntBuffer y2);

    /**
     * @param handle the TessPageIterator instance
     * @param orientation orientation value
     * @param writing_direction writing direction value
     * @param textline_order text line order
     * @param deskew_angle deskew angle
     */
    void TessPageIteratorOrientation(TessAPI.TessPageIterator handle, IntBuffer orientation,
            IntBuffer writing_direction, IntBuffer textline_order, FloatBuffer deskew_angle);

    /**
     * Deletes result iterator.
     * 
     * @param handle the TessResultIterator instance
     */
    void TessResultIteratorDelete(TessAPI.TessResultIterator handle);

    /**
     * @param handle the TessResultIterator instance
     * @return copy object
     */
    TessAPI.TessResultIterator TessResultIteratorCopy(TessAPI.TessResultIterator handle);

    /**
     * @param handle the TessResultIterator instance
     * @return tesseract result page iterator
     */
    TessAPI.TessPageIterator TessResultIteratorGetPageIterator(TessAPI.TessResultIterator handle);

    /**
     * @param handle the TessResultIterator instance
     * @return page iterator constant
     */
    TessAPI.TessPageIterator TessResultIteratorGetPageIteratorConst(TessAPI.TessResultIterator handle);

    /**
     * @param handle the TessResultIterator instance
     * @param level tesseract page level
     * @return text utf8 formatted
     */
    Pointer TessResultIteratorGetUTF8Text(TessAPI.TessResultIterator handle, int level);

    /**
     * @param handle the TessResultIterator instance
     * @param level tesseract page level
     * @return confidence
     */
    float TessResultIteratorConfidence(TessAPI.TessResultIterator handle, int level);

    /**
     * @param handle the TessResultIterator instance
     * @param is_bold font attribute
     * @param is_italic font attribute
     * @param is_underlined font attribute
     * @param is_monospace font attribute
     * @param is_serif font attribute
     * @param is_smallcaps font attribute
     * @param pointsize font attribute
     * @param font_id font attribute
     * @return word font attributes
     */
    String TessResultIteratorWordFontAttributes(TessAPI.TessResultIterator handle, IntBuffer is_bold,
            IntBuffer is_italic, IntBuffer is_underlined, IntBuffer is_monospace, IntBuffer is_serif,
            IntBuffer is_smallcaps, IntBuffer pointsize, IntBuffer font_id);

    /**
     * @param handle the TessResultIterator instance
     * @return 1 if word is from dictionary
     */
    int TessResultIteratorWordIsFromDictionary(TessAPI.TessResultIterator handle);

    /**
     * @param handle the TessResultIterator instance
     * @return 1 if word is numeric
     */
    int TessResultIteratorWordIsNumeric(TessAPI.TessResultIterator handle);

    /**
     * @param handle the TessResultIterator instance
     * @return 1 if symbol is superscript
     */
    int TessResultIteratorSymbolIsSuperscript(TessAPI.TessResultIterator handle);

    /**
     * @param handle the TessResultIterator instance
     * @return 1 if symbol is subscript
     */
    int TessResultIteratorSymbolIsSubscript(TessAPI.TessResultIterator handle);

    /**
     * @param handle the TessResultIterator instance
     * @return 1 if symbol is dropcap
     */
    int TessResultIteratorSymbolIsDropcap(TessAPI.TessResultIterator handle);

    public static class TessBaseAPI extends PointerType {

        public TessBaseAPI(Pointer address) {
            super(address);
        }

        public TessBaseAPI() {
            super();
        }
    };

    public static class ETEXT_DESC extends PointerType {

        public ETEXT_DESC(Pointer address) {
            super(address);
        }

        public ETEXT_DESC() {
            super();
        }
    };

    public static class TessPageIterator extends PointerType {

        public TessPageIterator(Pointer address) {
            super(address);
        }

        public TessPageIterator() {
            super();
        }
    };

    public static class TessMutableIterator extends PointerType {

        public TessMutableIterator(Pointer address) {
            super(address);
        }

        public TessMutableIterator() {
            super();
        }
    };

    public static class TessResultIterator extends PointerType {

        public TessResultIterator(Pointer address) {
            super(address);
        }

        public TessResultIterator() {
            super();
        }
    };
}
