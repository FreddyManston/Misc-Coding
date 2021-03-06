function wp_navbar_menub_stack(m_root)
{
	this.element = [];
	this.m_root = m_root;
}

function wp_navbar_menub(menubarid, navtree, options, styleOptions)
{
	var me = this;
	me.m_timeout = null;

	me.showItem = function(e,bShow)
	{
		function doslowfadein()
		{
			e.m_opacity += bShow ? 10 : -10;

			if( ( bShow && e.m_opacity >= me.styleOptions.level1.m_iOpacity ) || ( !bShow && e.m_opacity <= 0 ) )
			{
				clearInterval(e.m_interval);
				e.m_interval = e.m_opacity = undefined;
				if( !bShow )
				{	e.style.visibility = 'hidden'; }
				wp_navbar_menub.setOpacity(e,me.styleOptions.level1.m_iOpacity);
			}
			else
			{
				wp_navbar_menub.setOpacity(e,e.m_opacity);
			}
		}

		if( me.styleOptions.level1.m_bFade )
		{	
			if( e.m_interval !== undefined )
			{	clearInterval(e.m_interval); }
			else
			{	e.m_opacity = bShow ? 0 : me.styleOptions.level1.m_iOpacity; }
			e.style.visibility = '';
			wp_navbar_menub.setOpacity(e,e.m_opacity);
			e.m_interval = setInterval( doslowfadein, 75 / me.styleOptions.level1.m_iFadeSpeed );
		}
		else
		{	e.style.visibility = bShow ? '' : 'hidden'; }
	};
	me.mouseOver = function( id )
	{
		function domouseover()
		{
			clearTimeout(me.m_timeout);
			me.m_stack.pushMenu(id);
		}
		return domouseover;
	};
	me.mouseOut = function()
	{
		function domouseout()
		{
			function dotimeout()
			{
				me.m_stack.pushMenu(null);
			}
			clearTimeout(me.m_timeout);
			me.m_timeout = setTimeout( dotimeout,me.options.m_iTimeOut );
		}
		return domouseout;
	};
	me.subMenuClick = function()
	{
		function dosubmenuclick()
		{
			clearTimeout(me.m_timeout);
			me.m_stack.pushMenu(null);
		}
		return dosubmenuclick;
	};
	me.selectStart = function()
	{	return false; };

	me.options =
	{	'm_bIsPreview':false,
		'm_sCssClass':'',
		'm_sScriptId':'',
		'm_bNoScript':false,
		'm_bStaticScript':false,
		'm_bVertical':false,
		'm_iTimeOut':500,
		'm_bPopupBelow':1,
		'm_bPopupRight':1,
		'm_iPopupAlignmentH':0,
		'm_iPopupAlignmentV':0
	};
	if( options )
	{	me.options = WpNavBar.mergeOptions( me.options, options ); }
	me.styleOptions =
	{
		'm_iMaxCssLevel':1,
		'top':
		{
			'm_iHorizontalPosition':0,
			'm_iVerticalPosition':0,
			'm_bWrap':true,
			'm_bButtonsSameSize':true,
			'm_bStretchToWidth':false,
			'm_iHorizontalPadding':0,
			'm_iVerticalPadding':0,
			'm_iHorizontalButtonSeparation':0,
			'm_iVerticalButtonSeparation':0,			
			'withSubMenu':
			{
				'm_bHasImages':false,
				'm_bImagesOnRight':true
			}
		},
		'level1':
		{
			'm_iFirstPopupOffset':1,
			'm_iInterPopupOffset':1,
			'm_bFirstPopupSameSize':true,
			'm_iMinWidth':0,
			'm_iOpacity':100,
			'm_bFade':false,
			'm_iFadeSpeed':5,
			'separator':
			{
				'm_bAllowSeparators':true
			}
		}
	};
	if( styleOptions )
	{	me.styleOptions = WpNavBar.mergeOptions( me.styleOptions, styleOptions ); }

	if( navtree === null || navtree.childArray === null )
	{	return;	}

	var elemDiv = document.getElementById( menubarid );
	if( elemDiv === null )
	{
		var elemScript = document.getElementById( this.options.m_sScriptId );
		if( elemScript === null )
		{	return; }
		
		elemDiv = document.createElement( 'DIV' );
		elemScript.parentNode.insertBefore(elemDiv,elemScript);
		elemDiv.id = this.options.m_sId;
		elemDiv.className = this.options.m_sCssClass;
		elemDiv.style.position = 'absolute';
		elemDiv.style.left = this.options.m_iLeft+'px';
		elemDiv.style.top = this.options.m_iTop+'px';
		elemDiv.style.width = this.options.m_iWidth+'px';
		elemDiv.style.height = this.options.m_iHeight+'px';	
	}
	else
	{
		this.options.m_sCssClass = elemDiv.className;
	}

	me.createBar = function(menubarid, navtree, elemDiv)
	{
		if( !navtree || !navtree.childArray )
		{	return;	}
		
		var bAnyChildren = wp_navbar_menub.hasChildren( navtree );	
		var buttons = me.addTree(elemDiv, menubarid, null, null, navtree, 0, false, me.styleOptions.top.withSubMenu.m_bHasImages, (me.options.m_bVertical && bAnyChildren), !me.options.m_bNoScript );

		function dolayout()
		{
			me.sizeButtons(buttons);
			var brs = elemDiv.getElementsByTagName("br");
			for( var index = 0; index < brs.length; index+=1 )
			{	elemDiv.removeChild(brs[index]); }
			me.layoutButtons(elemDiv,buttons);
		}
		if( me.options.m_bIsPreview || me.options.m_bNoScript )
		{	dolayout(); }
		else
		{	setTimeout( dolayout,0 ); }
	};

	me.createMenu = function( elemDiv, idParent, idMenu, parenttree, level )
	{
		var bAnyChildren = wp_navbar_menub.hasChildren( parenttree );
		var cssLevel = (level > me.styleOptions.m_iMaxCssLevel) ? me.styleOptions.m_iMaxCssLevel : level;
		var levelStyleOptions = me.styleOptions[ 'level' + cssLevel ];
		var elemTable = document.createElement('TABLE');
		elemTable.className = me.options.m_sCssClass + '_level' + cssLevel;
		elemTable.id = idMenu;
		elemTable.style.visibility='hidden';
		wp_navbar_menub.setOpacity(elemTable,levelStyleOptions.m_iOpacity);
		elemTable.style.zIndex='140';
		elemTable.style.position='absolute';

		elemDiv.parentNode.insertBefore(elemTable, elemDiv);
		var elemTBody = document.createElement('TBODY');
		elemTable.appendChild( elemTBody );
		me.addTree(elemDiv, idMenu, elemTBody, idParent, parenttree, level, this.styleOptions.level1.separator.m_bAllowSeparators, true, bAnyChildren, true );
	};

	me.addTree = function(elemDiv, idMenu, elemTBody, idParent, navtree, level, allowSeparators, allowImage, forceImage, allowSubMenus)
	{
		var linkArray = [];
		for( var index = 0; index < navtree.childArray.length; index+=1 )
		{
			if(level===0&&index!==0){elemDiv.appendChild(document.createElement('BR'));}
			var treeitem = navtree.childArray[index];
			var bHasChildren = treeitem.childArray && (treeitem.childArray.length > 0 );
			var idItem = idMenu + '_I' + index.toString();
			var idSubMenu = ( bHasChildren ) ? ( idMenu + '_P' + index.toString() ) : null;
			var addImage = ( bHasChildren || forceImage ) && allowImage;
			if( treeitem.bSeparator && allowSeparators )
			{	me.addLink(elemDiv, idMenu, elemTBody, idParent, idItem+'_sep', null, null, true, level, false, false); }
	
			var elemItem = me.addLink(elemDiv,idMenu,elemTBody,idParent,idItem,treeitem,idSubMenu,false,level,addImage,allowSubMenus);
			me.addRollover( elemItem, idItem );
			linkArray.push( elemItem );
		}
		return linkArray;
	};

	me.addLink = function(elemDiv,idMenu,elemTBody,idParent,idItem,menuitem,idSubMenu,bSeparator,level,addImage,allowSubMenus)
	{
		var elemParent = (level===0) ? elemDiv : me.addRow(elemTBody, idItem);
		var elemItem = null;
		if(bSeparator)
		{	elemItem = document.createElement('HR'); }
		else if(menuitem.sUrl==='')
		{	elemItem = document.createElement('SPAN'); }
		else
		{
			elemItem = document.createElement('A');
			elemItem.href = menuitem.sUrl;
			elemItem.target = ( menuitem.sTarget ) ? menuitem.sTarget : '_self';
		}
		elemItem.m_depth = level;
		elemItem.m_parent = (level===0) ? null : idParent;
		elemItem.id = idItem;
		elemItem.className = '';

		if(!bSeparator)
		{
			if( level === 0 )
			{	elemItem.style.visibility='hidden'; }
			else 			
			{	elemItem.style.display = 'block'; }
			var elemText = document.createTextNode(menuitem.sTitle);
			elemItem.appendChild( elemText );
			elemItem.m_idchild = idSubMenu;
			elemItem.m_menu = (level===0) ? null : idMenu;
			if( idSubMenu )
			{	elemItem.className += ' hassubmenu'; }
			if( addImage )
			{
				var imageOnRight = ( level===0 && !me.options.m_bVertical ) ? me.styleOptions.top.withSubMenu.m_bImagesOnRight : me.options.m_bPopupRight;
				if( idSubMenu )
				{
					if( level===0 && !me.options.m_bVertical )
					{	elemItem.className += ( me.options.m_bPopupBelow ) ? ' hassubmenu_below' : ' hassubmenu_above'; }
					elemItem.className += imageOnRight ? ' hassubmenu_right' : ' hassubmenu_left';
				}
				var elemImg = document.createElement('IMG');
				elemImg.src='';
				elemImg.style.borderStyle='none';
				elemImg.style.visibility='hidden';
				elemImg.style.height='1px';
				if( imageOnRight )
				{	elemItem.appendChild(elemImg); }
				else
				{	elemItem.insertBefore(elemImg,elemText); }
			}
			if( menuitem.bIsCurrentPage === true )
			{	elemItem.className += ' currentpage'; }
		}
		elemParent.appendChild( elemItem );
		if( idSubMenu && allowSubMenus )
		{	me.createMenu( elemDiv, idItem, idSubMenu, menuitem, level+1 ); }		
		return elemItem;
	};

	me.addRow = function(elemTBody, idItem)
	{
		var elemTr = elemTBody.insertRow(-1);
		var elemTd = elemTr.insertCell(-1);
		me.addRollover(elemTd, idItem);
		elemTd.onclick = me.subMenuClick();
		return elemTd;
	};

	me.addRollover = function(e,idItem)
	{
		e.onmouseover = me.mouseOver( idItem );
		e.onmouseout = me.mouseOut();
		e.onselectstart = me.selectStart;	
	};
	
	me.sizeButtons = function(buttons)
	{
		var maxWidth = 0;
		var maxHeight = 0;
		
		for( var index = 0; index < buttons.length; index+=1 )
		{
			var elemA = buttons[index];
			if( elemA.offsetWidth > maxWidth )
			{	maxWidth = elemA.offsetWidth; }
			if( elemA.offsetHeight > maxHeight )
			{	maxHeight = elemA.offsetHeight; }
		}
		for( var index = 0; index < buttons.length; index+=1 )
		{
			var elemA = buttons[index];
			elemA.style.position='absolute';
			if( me.options.m_bVertical || me.styleOptions.top.m_bButtonsSameSize )
			{	wp_navbar_menub.setOffsetWidth( elemA, maxWidth ); }
			if( !me.options.m_bVertical || me.styleOptions.top.m_bButtonsSameSize )
			{	wp_navbar_menub.setOffsetHeight( elemA, maxHeight ); }
		
			var menuWidth = -1;
			if( !me.options.m_bVertical && me.styleOptions.level1.m_bFirstPopupSameSize )
			{	menuWidth = elemA.offsetWidth; }
			if( menuWidth < me.styleOptions.level1.m_iMinWidth )
			{	menuWidth = me.styleOptions.level1.m_iMinWidth; }
			if( menuWidth !== -1 )
			{
				var idMenu = elemA.m_idchild;
				if( idMenu )
				{
					var elemMenu = document.getElementById(idMenu);
					if( elemMenu )
					{
						if( elemMenu.offsetWidth < menuWidth )
						{	elemMenu.style.width = menuWidth + 'px'; }
					}
				}
			}
		}	
	};
	
	me.layoutButtons = function(elemDiv,buttons)
	{
		var divWidth = elemDiv.offsetWidth - 2 * me.styleOptions.top.m_iHorizontalPadding;
		var divHeight = elemDiv.offsetHeight - 2 * me.styleOptions.top.m_iVerticalPadding;

		var buttonRuns = [];
		var runWidth = [];
		var runHeight = [];
		var totalHeight = 0;
		var totalWidth = 0;

		var runIndex = 0;
		buttonRuns[ runIndex ] = [];
		runWidth[ runIndex ] = 0;
		runHeight[ runIndex ] = 0;

		for( var index = 0; index < buttons.length; index+=1 )
		{
			var elemA = buttons[index];
			var startRun = me.options.m_bVertical ?
				 ( runHeight[ runIndex ] + elemA.offsetHeight + me.styleOptions.top.m_iVerticalButtonSeparation > divHeight ) :
				 ( runWidth[ runIndex ] + elemA.offsetWidth + me.styleOptions.top.m_iHorizontalButtonSeparation > divWidth );
			if( me.styleOptions.top.m_bWrap && buttonRuns[runIndex].length > 0 && startRun )
			{
				totalWidth += runWidth[ runIndex ];
				totalHeight += runHeight[runIndex];
				if( me.options.m_bVertical )
				{ totalWidth+=me.styleOptions.top.m_iHorizontalButtonSeparation; }
				else
				{ totalHeight+=me.styleOptions.top.m_iVerticalButtonSeparation; }
	
				runIndex = runIndex + 1;
				buttonRuns[ runIndex ] = [];
				runWidth[ runIndex ] = 0;
				runHeight[ runIndex ] = 0;
			}
			if( me.options.m_bVertical )
			{
				runWidth[runIndex] = Math.max( elemA.offsetWidth, runWidth[runIndex] );
				runHeight[runIndex] += elemA.offsetHeight;
			}
			else
			{
				runWidth[runIndex] += elemA.offsetWidth;
				runHeight[runIndex] = Math.max( elemA.offsetHeight, runHeight[runIndex] );
			}
			if( buttonRuns[runIndex].length > 0 )
			{
				if( me.options.m_bVertical )
				{ runHeight[runIndex] += me.styleOptions.top.m_iVerticalButtonSeparation; }
				else
				{ runWidth[runIndex] += me.styleOptions.top.m_iHorizontalButtonSeparation; }
			}
			buttonRuns[runIndex].push( elemA );
		}
		totalWidth += runWidth[ runIndex ];
		totalHeight += runHeight[runIndex];

		if( me.styleOptions.top.m_bStretchToWidth )
		{
			if( me.options.m_bVertical )
			{
				var space = divWidth;
				for( var runIndex = 0; runIndex < buttonRuns.length; runIndex+=1 )
				{
					var colWidth = space / (buttonRuns.length - runIndex );
					if( runIndex !== 0 )
					{	colWidth -= me.styleOptions.top.m_iHorizontalButtonSeparation; }
					for( var elemIndex = 0; elemIndex < buttonRuns[runIndex].length; elemIndex+=1 )
					{
						var elemA = buttonRuns[runIndex][elemIndex];
						wp_navbar_menub.setOffsetWidth( elemA, colWidth );
					}
					space -= colWidth;
					runWidth[ runIndex ] = colWidth;
				}
			}
			else
			{
				for( var runIndex = 0; runIndex < buttonRuns.length; runIndex+=1 )
				{
					var excess = divWidth - runWidth[ runIndex ];
					var space = divWidth;
					var comp = runWidth[ runIndex ];
					for( var elemIndex = 0; elemIndex < buttonRuns[runIndex].length; elemIndex+=1 )
					{
						var elemA = buttonRuns[runIndex][elemIndex];
						var prop = space / comp;
						comp -= elemA.offsetWidth;
						var extra = ( elemA.offsetWidth * prop ) - elemA.offsetWidth;
						if( extra > excess )
						{	extra = excess; }
						wp_navbar_menub.setOffsetWidth( elemA, elemA.offsetWidth + extra );
						excess -= extra;
						runWidth[ runIndex ] += extra;
						space -= elemA.offsetWidth;
					}
					runWidth[ runIndex ] = divWidth;
				}
			}
			totalWidth = Math.max(totalWidth, divWidth);
		}

		var xOffset = me.styleOptions.top.m_iHorizontalPadding;
		var yOffset = me.styleOptions.top.m_iVerticalPadding;
		for( var runIndex = 0; runIndex < buttonRuns.length; runIndex+=1 )
		{
			if( this.options.m_bVertical )
			{	yOffset = me.styleOptions.top.m_iVerticalPadding; }
			else
			{	xOffset = me.styleOptions.top.m_iHorizontalPadding; }
			var width = me.options.m_bVertical ? totalWidth : runWidth[runIndex];
			var height = me.options.m_bVertical ? runHeight[runIndex] : totalHeight;
			var xLeft =	( me.styleOptions.top.m_iHorizontalPosition === 1 ) ? ( divWidth - width ) / 2 :
						( me.styleOptions.top.m_iHorizontalPosition === 2 ) ? divWidth - width :
						0;
			var yTop =	( me.styleOptions.top.m_iVerticalPosition === 1 ) ? ( divHeight - height ) / 2 :
						( me.styleOptions.top.m_iVerticalPosition === 2 ) ? divHeight - height :
						0;
			for( var elemIndex = 0; elemIndex < buttonRuns[runIndex].length; elemIndex+=1 )
			{
				var elemA = buttonRuns[runIndex][elemIndex];
				elemA.style.top = (yTop + yOffset )+'px';
				elemA.style.left = (xLeft + xOffset) + 'px';
				if( me.options.m_bVertical )
				{	yOffset += elemA.offsetHeight + me.styleOptions.top.m_iVerticalButtonSeparation; }
				else
				{	xOffset += elemA.offsetWidth + me.styleOptions.top.m_iHorizontalButtonSeparation; }
				elemA.style.position='absolute';
				elemA.style.visibility='';
			}
			if( this.options.m_bVertical )
			{	xOffset += runWidth[runIndex] + me.styleOptions.top.m_iHorizontalButtonSeparation; }
			else
			{	yOffset += runHeight[ runIndex ] + me.styleOptions.top.m_iVerticalButtonSeparation; }
		}
	};

	me.m_stack = new wp_navbar_menub_stack(me);
	me.createBar(menubarid, navtree, elemDiv);

	if( this.options.m_bNoScript )
	{
		external.NavNoScriptWrite(elemDiv.outerHTML + '\n');
		external.NavNoScriptComplete();
	}
}

wp_navbar_menub.setOffsetWidth = function( elem, desiredWidth )
{
	elem.style.width = desiredWidth + 'px';
	var iNewOffsetWidth = elem.offsetWidth;
	var iNewWidth = (desiredWidth*2 - iNewOffsetWidth);
	elem.style.width = iNewWidth + 'px';
};

wp_navbar_menub.setOffsetHeight = function( elem, desiredHeight )
{
	elem.style.height = desiredHeight + 'px';
	var iNewOffsetHeight = elem.offsetHeight;
	var iNewHeight = (desiredHeight*2 - iNewOffsetHeight);
	elem.style.height = iNewHeight + 'px';
};

wp_navbar_menub.setOpacity = function(e,opacity)
{
	e.style.opacity = (opacity==100) ? '' : (opacity / 100);
	e.style.filter = (opacity==100) ? '' : 'alpha(opacity=' + opacity +')';
};

wp_navbar_menub.hasChildren = function(navtree)
{
	for( var index = 0; index < navtree.childArray.length; index+=1 )
	{
		var treeitem = navtree.childArray[index];
		if( treeitem.childArray && (treeitem.childArray.length > 0 ) )
		{	return true; }
	}
	return false;
};

wp_navbar_menub_stack.prototype.pushMenu = function(idelem)
{
	var newelements = [];
	for( var id = idelem; id; )
	{
		newelements.unshift(id);
		id = document.getElementById(id).m_parent;
	}

	for( var index = this.element.length - 1; index >= 0; index -= 1 )
	{
		if( (index >= newelements.length) || (this.element[index] != newelements[index] ) )
		{	this.popMenu(); }
		else
		{	break; }
	}
	index += 1;

	for( ; index < newelements.length; index += 1 )
	{	
		var e = document.getElementById(newelements[index]);
		e.className += ' open';
		this.element.push(e.id);
		if(e.m_idchild)
		{	this.showMenu(e); }
	}
};

wp_navbar_menub_stack.prototype.popMenu = function()
{
	if(this.element.length>0)
	{
		var e = document.getElementById( this.element.pop() );
		var i = e.className.lastIndexOf( ' open' );
		if( i !== -1 )
		{	e.className = e.className.substring(0,i); }

		if(e.m_idchild)
		{	this.m_root.showItem( document.getElementById(e.m_idchild), false); }
	}
};

wp_navbar_menub_stack.prototype.showMenu = function(p)
{
	var e = document.getElementById(p.m_idchild);
	var pm = (p.m_menu) ? document.getElementById(p.m_menu) : p;
	var top = 0; var left = 0;

	if( p.m_depth !== 0 )
	{	top -= e.childNodes[0].childNodes[0].offsetTop; }
	var c = p;	while(c && c.style.position!=='relative') { top += c.offsetTop; c = c.offsetParent; }
	c = pm;	while(c && c.style.position!=='relative') { left += c.offsetLeft; c = c.offsetParent; }
	if(this.m_root.options.m_bVertical || p.m_depth !== 0)
	{
		var k = p.m_depth === 0 ? this.m_root.styleOptions.level1.m_iFirstPopupOffset : this.m_root.styleOptions.level1.m_iInterPopupOffset;
		if(this.m_root.options.m_bPopupRight)
		{	left += pm.offsetWidth + k;	}
		else
		{	left -= e.offsetWidth + k; }
		if(!this.m_root.options.m_bPopupBelow)
		{	top -= (e.offsetHeight - p.offsetHeight - (this.m_root.styleOptions.level1.m_iInterPopupOffset*2)); }
	}
	else
	{
		if( p.m_depth === 0 )
		{
			if(this.m_root.options.m_bPopupBelow)
			{	top += p.offsetHeight + this.m_root.styleOptions.level1.m_iFirstPopupOffset; }
			else
			{	top -= (e.offsetHeight + this.m_root.styleOptions.level1.m_iFirstPopupOffset); }
		}
	}

	if( p.m_depth === 0 )
	{
		if(this.m_root.options.m_bVertical)
		{
			if(this.m_root.options.m_iPopupAlignmentV === 1)
			{	top += (p.offsetHeight - e.offsetHeight) / 2; }
			else if(this.m_root.options.m_iPopupAlignmentV === 2)
			{	top += p.offsetHeight - e.offsetHeight; }
		}
		else
		{
			if(this.m_root.options.m_iPopupAlignmentH === 1)
			{	left += (p.offsetWidth - e.offsetWidth) / 2; }
			else if(this.m_root.options.m_iPopupAlignmentH === 2)
			{	left += p.offsetWidth - e.offsetWidth; }

		}
	}
	top = Math.max( 0, Math.min( document.body.scrollTop+document.body.clientHeight-e.offsetHeight, top ) );
	left = Math.max( 0, Math.min( document.body.scrollLeft+document.body.clientWidth-e.offsetWidth, left ) );
	e.style.top = top+'px'; e.style.left = left+'px';
	this.m_root.showItem(e,true);
};
