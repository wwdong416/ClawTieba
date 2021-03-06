public class IDCReaderSDK {
	
	private static final String TAG = "unpack";

	/* this is used to load the 'wltdecode' library on application
     */
    static {
        System.loadLibrary("wltdecode");
    }

	public IDCReaderSDK() {
		/*if( 0==wltInit("") )
        	Log.i(TAG,  "wltInit success");*/
	}
	public static int Init(){
		//return wltInit("/wltlib");
// wltInit(Environment.getExternalStorageDirectory() + "/wltlib");
		return wltInit("D:/wltlib");
//		return 1;
	}
	public static int unpack(byte[] wltdata, byte[] licdata){
		return wltGetBMP(wltdata, licdata);
	}

	// native functin interface
    public static native int wltInit(String workPath);
    
    public static native int wltGetBMP(byte[] wltdata, byte[] licdata);
    
}
