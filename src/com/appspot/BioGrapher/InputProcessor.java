package com.appspot.BioGrapher;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * The basic interface that do DC-THERA Directory resource processing. A processor just does something, typically
 * with one or more knowledge bases (i.e., a Jena {@link Model}).
 * 
 * This someting is done by the {@link #run()} method. We set the convention that a class implementing this interface
 * receive parameter values via JavaBean setter methods, eg, setPropA ( 10 ) sets 10 for the property propA, which 
 * are invoked before run(), and, after that, the they return values via getter methods, to be invoked after run(), 
 * eg, getPropB() returns the value of propB.
 * 
 * It is natural to make this interface an empty version of {@link Runnable} (which already contains run(), cause it 
 * has the same shape. We don't extend Runnable directly, cause we want the ability to write interfaces were we
 * specifically require a knowledge processor and not a generic runnable.
 *
 * <dl><dt>date</dt><dd>Feb 28, 2012</dd></dl>
 *
 */
public interface InputProcessor extends Runnable
{
}
