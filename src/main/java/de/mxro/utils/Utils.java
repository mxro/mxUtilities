package de.mxro.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.event.MouseInputListener;
import javax.swing.text.JTextComponent;

import mx.gwtutils.MxroGWTUtils;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import de.mxro.filesystem.File;
import de.mxro.filesystem.FileSystemObject;
import de.mxro.filesystem.Folder;
import de.mxro.filesystem.RootFolder;
import de.mxro.filesystem.v01.LocalFolder;
import de.mxro.string.filter.Filter;
import de.mxro.utils.log.UserError;



public class Utils {
	
	/**
	 * remove tags, line breaks and leading spaces
	 */
	private final static Filter TEXT_ONLY = /*new Xslt(
					"<xsl:template match='/'><xsl:value-of select='*' /></xsl:template>\n",*/
					Filter.regExReplace("\\<.*?>|\n|^ *", "", Filter.identity);
	
	public static String removeMarkup(String xml) {
		return TEXT_ONLY.perform(xml);
	}
	
	public static Folder getOwnerFolder(Folder folder) {
		if (!(folder instanceof RootFolder))
			return folder.getOwner();
		if (folder instanceof LocalFolder) {
			try {
				return FileSystemObject.newLocalRootFolder(URIImpl.fromFile(folder.getURI().getFile().getParentFile()));
			} catch (final URISyntaxException e) {
				UserError.singelton.log(e);
				return null;
			}
		}
		
		throw new IllegalArgumentException("Unsupported Folder type!");
	}
	
	/**
	 * Adds keyboard shortcuts of actions in vector actions who have been specified by 
	 * putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(...);
	 * 
	 * @param actions
	 * @param comp
	 */
	public static void addKeyboardShortcuts(Vector<? extends Action> actions, JComponent comp) {
		for (final Action a : actions) {
			final Object o = a.getValue(Action.ACCELERATOR_KEY);
			final KeyStroke keyStroke = (KeyStroke) o;
			if (keyStroke == null) continue;
			//System.out.println("register Stroke: "+a.getClass().getName()+" "+keyStroke);
			comp.registerKeyboardAction(a, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
			if (comp instanceof JTextComponent) {
				((JTextComponent) comp).getKeymap().addActionForKeyStroke(keyStroke, a);
			}
		}
	}
	
	public static void addKeystrokesToBeanTree(Vector<? extends Action> actions, JComponent root) {
		
		addKeyboardShortcuts(actions, root);
		for (final Component c: root.getComponents()) {
			if (c instanceof JComponent)
				addKeystrokesToBeanTree(actions, (JComponent) c);
		}
	}
	
	public static Rectangle normalizeRectangle(int x, int y, int width, int height) {
		
		Point p1 = new Point(x, y);
		Point p2 = new Point(x+width, y +height);
		
		final Point p1neu = new Point(p1);
		final Point p2neu = new Point(p2);
		if (width < 0) {
			p1neu.x = p2.x;
			p1neu.y = p1.y;
			p2neu.x = p1.x;
			p2neu.y = p2.y;
		}
		p1 = new Point(p1neu);
		p2 = new Point(p2neu);
		if(height < 0) {
			p1neu.x = p1.x;
			p1neu.y = p2.y;
			p2neu.x = p2.x;
			p2neu.y = p1.y;
		}
		
		return new Rectangle(p1neu.x, p1neu.y, p2neu.x-p1neu.x, p2neu.y-p1neu.y);
	}
	
	public static Vector<Component> getComponentsInArea(Component rootComponent, Point p1, Point p2) {
		final Vector<Component> res = new Vector<Component>();
		final Rectangle rect = normalizeRectangle(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);
		// System.out.println("rect: "+rect);
		if (rootComponent instanceof Container) {
			for (final Component c : ((Container) rootComponent).getComponents()) {
				//System.out.println(rect + " : "+c.getX()+" "+c.getBounds());
			
				if (rect.intersects(c.getBounds())) {
					res.add(c);
					//System.out.println("intersects");
				}
			}
		}
		return res;
	}
	
	public static void addMouseInputListenerToAllChildComponents(Component forComponent, MouseInputListener listener) {
		if (forComponent instanceof Container) {
			for (final Component c : ((Container) forComponent).getComponents()) {
				c.addMouseMotionListener(listener);
				c.addMouseListener(listener);
				addMouseInputListenerToAllChildComponents(c, listener);
			}
		}
	}
	
	// function from "The Java Developers Almanac 1.4"
	//	 This method returns true if the specified image has transparent pixels
    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            final BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }
    
        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
         final PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (final InterruptedException e) {
        }
    
        // Get the image's color model
        final ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }
	
    // function from "The Java Developers Almanac 1.4"
	public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage)
			return (BufferedImage)image;
    
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
    
        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        final boolean hasAlpha = hasAlpha(image);
    
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        /*try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
    
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
            //System.out.println("Type: "+bimage.getType());
        } catch (HeadlessException e) {
            // The system does not have a screen
        }*/
    
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
    
        // Copy image to buffered image
        final Graphics g = bimage.createGraphics();
    
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
    
        return bimage;
    }
	
	public static boolean copyJarToFile(FileSystemObject dest, JarEntry entry, JarFile file) throws IOException {
		if (dest == null)
			throw new IllegalArgumentException("copyJarToFile: Destination was null.");
		if (entry == null)
			throw new IllegalArgumentException("copyJarToFile: JarEntry was null.");
		
		if (!entry.isDirectory()) {
			if (!(dest instanceof File)) {
				UserError.singelton.log("Utils.copyJarToFile: Destination must be a file.", UserError.Priority.HIGH);
				return false;
			}
			//System.out.println("not Dir.");
			streamCopy(((File) dest).getOutputStream(), file.getInputStream(entry));
			return true;
		}
		
		if (!(dest instanceof Folder)) {
			UserError.singelton.log("Utils.copyJarToFile: Destination must be a folder.", UserError.Priority.HIGH);
			return false;
		}
		final Folder newFolder = ((Folder) dest).forceFolder(MxroGWTUtils.lastElement(entry.getName(), "/"));
		if (newFolder == null) {
			UserError.singelton.log("Utils.copyJarToFile: Could not create folder "+MxroGWTUtils.lastElement(entry.getName(), "/"), UserError.Priority.HIGH);
			return false;
		}
		
		
		boolean  res = true;
		
		final Enumeration <JarEntry> entries = file.entries();
		while (entries.hasMoreElements()) {
			final JarEntry oneE = entries.nextElement();
			//System.out.println(oneE.getName()+" == "+entry.getName());
			if (oneE.getName().matches("^"+entry.getName()+".*")) {
				final String remainingName = oneE.getName().replaceAll("^"+entry.getName(), "");
				if (oneE.isDirectory()) {
					
						if (newFolder.forceFolder(URIImpl.create(remainingName)) == null) {
							UserError.singelton.log("Utils.copyJarToFile: Could not create Folder "+remainingName, UserError.Priority.LOW);
							res = false;
						}
					
				} else {
					Folder parent = null;
					
						//System.out.println("create parent folder: "+Utils.removeLastElement(oneE.getName(), "/")); 
						parent = newFolder.forceFolder(URIImpl.create(MxroGWTUtils.removeLastElement(remainingName, "/")));
					
					if (parent == null) {
						UserError.singelton.log("Utils.copyJarToFile: Could not create Folder "+MxroGWTUtils.removeLastElement(remainingName, "/"), UserError.Priority.LOW);
						res = false;
					} else {
						final File newFile = parent.forceFile(MxroGWTUtils.lastElement(remainingName, "/"));
						if (newFile == null) {
							UserError.singelton.log("Utils.copyJarToFile: Could not create File "+MxroGWTUtils.lastElement(remainingName, "/"), UserError.Priority.LOW);
							res = false;
						} else {
							streamCopy((newFile).getOutputStream(), file.getInputStream(oneE));
						}
					}
				}
				
			}
		}
        /*hile ((lEntry = stream.getNextJarEntry()) != null) {
        	System.out.println("copy lEntry "+lEntry.getName());
        	file.e
        	FileSystemObject newDest;
        	if (!lEntry.isDirectory()) {
        		newDest = newFolder.createFile(lEntry.getName());
        		res = res & copyJarToFile(newDest, lEntry, file);
        	}
        	newDest = newFolder.createFolder(lEntry.getName());
        	res = res & copyJarToFile(newDest, lEntry, file);
        }*/
		return res;
	}
	
	public static Folder selectLocalFolder(java.io.File root, boolean save) {
		final JFileChooser chooser = new JFileChooser(root);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Select directory for export");
	
		int returnVal;
		if (!save) {
			returnVal = chooser.showOpenDialog(null);
		} else {
			returnVal = chooser.showSaveDialog(null);
		}
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			final java.io.File file = chooser.getSelectedFile();
			file.mkdirs();
			try {
				return FileSystemObject.newLocalRootFolder(URIImpl.fromFile(file));
			} catch (final URISyntaxException e) {
				UserError.singelton.log(e);
			}
		}
		return null;
	}
	
	public static void pln(String s) {
		System.out.println(s);
	}
	
	public static String assertAtEnd(String s, char atEnd) {
		if (s.length()==0) return String.valueOf(atEnd);
		
		if (s.charAt(s.length()-1) == atEnd)
			return s;
		return s + atEnd;
	}
	
	public static void streamCopy(OutputStream os, InputStream is) throws IOException {
		final byte[] buffer = new byte[255];
		int read=0;
		while ((read=is.read(buffer)) != -1) {
			os.write(buffer, 0, read);
		}
	}

    public static String fromInputStream(InputStream is, String encoding) throws IOException {
       final ByteArrayOutputStream os = new ByteArrayOutputStream();
		streamCopy(os, is);
		return os.toString(encoding);
    }

    /**
     * turns the stream into a string using encoding "UTF-8"
     * @param is
     * @return
     * @throws java.io.IOException
     */
	public static String fromInputStream(InputStream is) throws IOException {
		 return fromInputStream(is, "UTF-8");
		
	}
	
	public static String getApplePropertyListKey(InputStream properties, String key) throws IOException {
		final String props = fromInputStream(properties);
		final int idx = props.indexOf("<key>"+key+"</key>");
		if (idx >= 0)
			return props.substring(idx+("<key>"+key+"</key>").length(), props.indexOf("<key>", idx+6));
		
		de.mxro.utils.log.UserError.singelton.log("Properties File has no <key>-tag ("+props+")");
		return null;
	}
	
	public static String getApplePropertyListStringKey(InputStream properties, String key) throws IOException {
		final String exKey = getApplePropertyListKey(properties, key);
		if (exKey == null)
			return null;
		return exKey.substring(exKey.indexOf("<string>")+"<string>".length(), exKey.indexOf("</string>"));
	}
	
	public static String[] getApplePropertyListStringKeyArray(InputStream properties, String key) throws IOException {
		final String exKey = getApplePropertyListKey(properties, key);
		final Vector<String> vector = new Vector<String>();
		int idx=0;
		while ((idx=exKey.indexOf("<string>", idx))!=-1) {
			vector.add(exKey.substring(idx+"<string>".length(), exKey.indexOf("</string>", idx)));
			idx++;
		}
		String [] res= new String[vector.size()];
		res = vector.toArray(res);
		
		return res;
	}
	
	public static String stringArrayToString(String[] sa) {
		String res = "";
		if (sa.length > 0) {
			for (final String s: sa) {
				res = res + "[" + s+ "]";
			}
		}
		return res;
	}
	
	
	/**
	 * 
	 * makes path relative and in difference to URI.relativize it works!
	 * 
	 * @param basePath
	 * @param toRelativize
	 * @return
	 */
	public static String relativize(String basePath, String toRelativize) {
		final int idx = basePath.lastIndexOf("/");
		if (idx >= 0) {
			basePath = basePath.substring(0, idx+1);
		}
		//de.mxro.UserError.singelton.log("basePath: "+basePath);
		return relativize(basePath, toRelativize, false);
	}
	
	private static String makeUnder(String basePath, String toRelativize) {
		if (basePath.equals( ""))
			return toRelativize;
		final String[] baseSegments = basePath.split("/");
		String nextBase=""; 
		if (baseSegments.length > 1) {
			for (int i=1;i<baseSegments.length;i++) {
				nextBase = nextBase + baseSegments[i] +"/";
			}
			nextBase = nextBase.substring(0, nextBase.lastIndexOf("/"));
		}
		
		
		return "../"+makeUnder(nextBase, toRelativize);
	}
	
	private static String relativize(String basePath, String toRelativize, boolean foundone) {
		if (basePath.equals( "") || basePath.equals("/"))
			return toRelativize;
		
		if (toRelativize.equals("/")) 
			return toRelativize;
		
		final String[] baseSegments = basePath.split("/");
		String nextBase=""; 
		if (baseSegments.length > 1) {
			for (int i=1;i<baseSegments.length;i++) {
				nextBase = nextBase + baseSegments[i] +"/";
			}
			nextBase = nextBase.substring(0, nextBase.lastIndexOf("/"));
		}
		
		final String[] toRealtivizeSegements = toRelativize.split("/");
		//if (toRealtivizeSegements.length == 1) return "../"+relativize(nextBase, toRelativize);
		
		
		
		//pln("baseSegments: "+stringArrayToString(baseSegments));
		//pln("toRealtivizeSegements: "+stringArrayToString(toRealtivizeSegements));
		
		
		
		String nextToRelativize="";
		if (toRealtivizeSegements.length > 1) {
			for (int i=1;i<toRealtivizeSegements.length;i++) {
				nextToRelativize = nextToRelativize + toRealtivizeSegements[i] +"/";
			}
			nextToRelativize = nextToRelativize.substring(0, nextToRelativize.lastIndexOf("/"));
		}
		
		//pln("nextBase: "+nextBase+" nextToRealtivize: "+nextToRelativize);
		if (toRealtivizeSegements.length == 0)
			throw new IllegalArgumentException("toRelativize: "+toRelativize+" has 0 segments!");
		
		if (baseSegments.length == 0)
			throw new IllegalArgumentException("basePath: "+basePath+" has 0 segments!");
		
		if (baseSegments[0].equals(toRealtivizeSegements[0])) {
			
			if (baseSegments.length == 1) {
				
				if (toRealtivizeSegements.length == 1)
					return ".";
			};
			
			
			return relativize(nextBase, nextToRelativize,true);
			
		} else {
			if (foundone)
				return "../"+makeUnder(nextBase, toRelativize);
			else
				return toRelativize;
		}
		// return toRelativize;
			
	}
	
	public static String removeFirstElement(String s, String separator) {
		return mx.gwtutils.MxroGWTUtils.removeFirstElement(s, separator);
	}
	
	public static void assertField(final String fieldname, final Class inclass) {
		/*try { 
			inclass.getField(fieldname);
		} catch (NoSuchFieldException e) {
			de.mxro.UserError.singelton.showError("Field '"+fieldname+"' does not exist in class "+inclass.getName());
		}*/
	}
	
	
	
	public static String getPackageDirectory(Class forclass) {
		final ClassLoader loader = forclass.getClassLoader();
		if (loader == null)
			throw new IllegalArgumentException(forclass.getName()+" has no classloader!");
		return new java.io.File(loader.getResource( forclass.getName().replace('.', '/')+".class").getFile()).getParent()+"/";
	}
	
	public static final int MACOSX = 1;
	public static final int WINDOWS = 2;
	public static final int UNKNOWN = 3;
	
	public static int getOperatingSystem() {
		final String os = System.getProperty("os.name");
		if (os.contains("Windows"))
			return WINDOWS;
		if (os.equals("Mac OS X"))
			return MACOSX;
	
		return UNKNOWN;
	}
	
	public static String domToString(org.w3c.dom.Document doc) {
		final XMLSerializer serializer = new XMLSerializer();
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		serializer.setOutputByteStream(bos);
        try {
			serializer.serialize(doc);
		} catch (final IOException e) {
			return "";
		}
		return bos.toString();
	}
	
	/**
	 * Centers the component <CODE>c</CODE> on component <CODE>p</CODE>.  
	 * If <CODE>p</CODE> is <CODE>null</CODE>, the component <CODE>c</CODE> 
	 * will be centered on the screen.  
	 * 
	 * @param  c  the component to center
	 * @param  p  the parent component to center on or null for screen
	 * @see  #centerComponent(Component)
	 */
	public static void centerComponent(Component c, Component p) {
		if(c == null)
			return;
		final Dimension d = (p != null ? p.getSize() : 
			Toolkit.getDefaultToolkit().getScreenSize()
		);
		c.setLocation(
			Math.max(0, (d.getSize().width/2)  - (c.getSize().width/2)), 
			Math.max(0, (d.getSize().height/2) - (c.getSize().height/2))
		);
	}
	
	
	
}
