package infrastructure;

/*
 * Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2002.
 * All rights reserved. Software written by Ian F. Darwin and others.
 * $Id: LICENSE,v 1.8 2004/02/09 03:33:38 ian Exp $
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
 * pioneering role in inventing and promulgating (and standardizing) the Java 
 * language and environment is gratefully acknowledged.
 * 
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
 * inventing predecessor languages C and C++ is also gratefully acknowledged.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import events.EntitySelectEvent;
import events.EntitySelectEventListener;

import main.Entity;
import main.Character;
import main.Player;
import main.Scene;

/**
 * Display all entities in a scene by using a JTree view.
 */
public class SceneTree extends JPanel
{
	// The scene to map out.
	private Scene _Scene;
	// The tree.
	private JTree _Tree;
	// The currently selected node.
	private DefaultMutableTreeNode _SelectedNode;
	// The list of event listeners.
	private EventListenerList _EventListeners;

	/**
	 * Constructor for a scene tree.
	 * 
	 * @param scene
	 *            The scene to view.
	 * */
	public SceneTree(Scene scene)
	{
		setLayout(new BorderLayout());

		// Set the scene and initialize some variables.
		_Scene = scene;
		_EventListeners = new EventListenerList();

		// Make a tree list with all the nodes, and make it a JTree.
		_Tree = new JTree(mapScene(scene));
		_Tree.setFocusable(false);
		_Tree.getComponent(0).setFocusable(false);

		// Add a listener.
		_Tree.addTreeSelectionListener(new TreeSelectionListener()
		{
			public void valueChanged(TreeSelectionEvent e)
			{
				_SelectedNode = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

				if (getSelectedEntity() != null)
				{
					selectedEntityInvoke();
				}
			}
		});

		// Lastly, put the JTree into a JScrollPane.
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.getViewport().add(_Tree);
		add(BorderLayout.CENTER, scrollpane);
	}

	/**
	 * Maps out all of the scene's entities into a root node, ready to add to a JTree.
	 * 
	 * @param scene
	 *            The scene to map out.
	 * @return The root node of the scene.
	 */
	private DefaultMutableTreeNode mapScene(Scene scene)
	{
		// Create the root node.
		DefaultMutableTreeNode dir = new DefaultMutableTreeNode("Scene");

		// Add the top nodes to the root.
		DefaultMutableTreeNode pDir = new DefaultMutableTreeNode("Player");
		DefaultMutableTreeNode cDir = new DefaultMutableTreeNode("Character");
		DefaultMutableTreeNode eDir = new DefaultMutableTreeNode("Entity");
		dir.add(pDir);
		dir.add(cDir);
		dir.add(eDir);

		// For each entity in the scene, add them to their respective node in the tree.
		for (Entity entity : new ArrayList<Entity>(scene.getEntities()))
		{
			// Depending on the entity's sub-type class, put it under different nodes. Go from the most exclusive and upwards.
			if (entity instanceof Player)
			{
				pDir.add(new DefaultMutableTreeNode(entity));
			}
			else if (entity instanceof Character)
			{
				cDir.add(new DefaultMutableTreeNode(entity));
			}
			else
			{
				eDir.add(new DefaultMutableTreeNode(entity));
			}
		}

		// Return the root node.
		return dir;
	}

	/**
	 * This methods allows classes to register for entity select events.
	 * 
	 * @param listener
	 *            The listener class.
	 */
	public void addEventListener(EntitySelectEventListener listener)
	{
		_EventListeners.add(EntitySelectEventListener.class, listener);
	}

	/**
	 * This methods allows classes to unregister for entity select events.
	 * 
	 * @param listener
	 *            The listener class.
	 */
	public void removeEventListener(EntitySelectEventListener listener)
	{
		_EventListeners.remove(EntitySelectEventListener.class, listener);
	}

	/**
	 * Method for raising the selected event.
	 */
	protected void selectedEntityInvoke()
	{
		// For all listeners, enlighten them.
		for (EntitySelectEventListener listener : _EventListeners.getListeners(EntitySelectEventListener.class))
		{
			listener.handleEvent(new EntitySelectEvent(getSelectedEntity()));
		}
	}

	/**
	 * Update the tree.
	 */
	public void updateTree()
	{
		updateTree(_Scene);
	}

	/**
	 * Update the tree.
	 * 
	 * @param scene
	 *            The new scene.
	 */
	public void updateTree(Scene scene)
	{
		_Scene = scene;
		_Tree.setModel(new JTree(mapScene(scene)).getModel());
	}

	/**
	 * Get the currently selected node.
	 * 
	 * @return The currently selected node.
	 */
	public DefaultMutableTreeNode getSelectedNode()
	{
		return _SelectedNode;
	}

	/**
	 * Get the currently selected entity.
	 * 
	 * @return The currently selected entity.
	 */
	public Entity getSelectedEntity()
	{
		// If the selected node is a type of entity, return it.
		if (_SelectedNode.getUserObject() instanceof Entity) { return (Entity) _SelectedNode.getUserObject(); }

		// No valid entity, return null.
		return null;
	}

	public Dimension getMinimumSize()
	{
		return new Dimension(200, 400);
	}

	public Dimension getPreferredSize()
	{
		return new Dimension(200, 400);
	}
}