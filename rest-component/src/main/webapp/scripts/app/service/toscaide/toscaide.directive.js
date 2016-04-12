'use strict';

angular.module('cloudoptingApp').directive('cytoscape', function($rootScope,$http) {
    // graph visualisation by - https://github.com/cytoscape/cytoscape.js
    return {
        restrict: 'E',
        template :'<div id="cy"></div>',
        replace: true,
        scope: {
            // data objects to be passed as an attributes - for nodes and edges
            cyData: '=',
            cyEdges: '=',
            cyTemplates: '=',
            cyEdgetemplates: '=',
            // controller function to be triggered when clicking on a node
            cyClick:'&',
            cyEdgeclick:'&',
            cyRenamenode:'&',
            cyRemove:'&'
        },
        link: function(scope, element, attrs, fn) {
            // dictionary of colors by types. Just to show some design options

            // graph  build
            scope.doCy = function(){ // will be triggered on an event broadcast
                // initialize data object
                scope.elements = {};
                scope.elements.nodes = [];
                scope.elements.edges = [];
console.debug(scope.elements.nodes);
                // parse edges
                // you can build a complete object in the controller and pass it without rebuilding it in the directive.
                // doing it like that allows you to add options, design or what needed to the objects
                // doing it like that is also good if your data object/s has a different structure
				var i = 0;                
				for (i=0; i<scope.cyEdges.length; i++)
                {
                    // get edge source
                    var eSource = scope.cyEdges[i].source;
                    // get edge target
                    var eTarget = scope.cyEdges[i].target;
                    // get edge id
                    var eId = scope.cyEdges[i].id;
                    var eType = scope.cyEdges[i].type;
                    var eStyle = scope.cyEdgetemplates[eType].style;
                    var eProps = scope.cyEdgetemplates[eType].props;
                    var eColor = scope.cyEdgetemplates[eType].color;
                    console.debug("edge type:" + scope.cyEdges[i].type);
                    // build the edge object
                    var edgeObj = {
                        data:{
                        id:eId,
                        source:eSource,
                        target:eTarget,
                        color:eColor,
                        style:eStyle,
                        properties:eProps,
                        type: eType
                        }
                    };
                    // adding the edge object to the edges array
                    scope.elements.edges.push(edgeObj);
                }

                // parse data and create the Nodes array
                // object type - is the object's group
                
                for (i=0; i<scope.cyData.length; i++)
                {
                    // get id, name and type  from the object
                    var Oid = scope.cyData[i].id;
                    var Oname = scope.cyData[i].name;
					var Otype = scope.cyData[i].type;
					var Oshape = scope.cyTemplates[Otype].shape;
					var Oprops = scope.cyTemplates[Otype].props;
					var typeColor = scope.cyTemplates[Otype].color;
					var image = 'assets/type/'+scope.cyTemplates[Otype].image;
                    // build the object, add or change properties as you need - just have a name and id
                    var elementObj = {
                        group:Otype,'data':{
                            id:Oid,
                            name:Oname,
                            typeColor:typeColor,
                            image:image,
                            typeShape:Oshape,  //was Otype
							properties:Oprops,
                            type:Otype
                    }};
                    // add new object to the Nodes array
                    scope.elements.nodes.push(elementObj);
                }
                console.debug(scope.elements.nodes);
                // graph  initialization
                // use object's properties as properties using: data(propertyName)
                // check Cytoscapes site for much more data, options, designs etc
                // http://cytoscape.github.io/cytoscape.js/
                // here are just some basic options
                $('#cy').cytoscape({
                    layout: {
                        name: 'breadthfirst',
                        fit: true, // whether to fit the viewport to the graph
                        ready: undefined, // callback on layoutready
                        stop: undefined, // callback on layoutstop
                        padding: 5 // the padding on fit
                    },
                    style: cytoscape.stylesheet()
                        .selector('node')
                        .css({
                            'shape': 'data(typeShape)',
                            'width': '120',
                            'height': '90',
                            'background-color': 'data(typeColor)',
                            'background-image': 'data(image)',
                            'background-fit': 'contain',
                            'content': 'data(name)',
                            'text-valign': 'center',
                            'color': 'white',
                            'text-outline-width': 2,
                            'text-outline-color': '#000000'
                        })
                        .selector('edge')
                        .css({
                            'width': '5',
                            'target-arrow-shape': 'triangle',
                            'source-arrow-shape': 'none',
                            'content': 'data(type)',
                            'line-color': 'data(color)',
                            'line-style': 'data(style)'
                        })
                        .selector(':selected')
                        .css({
                            'background-color': 'black',
                            'line-color': 'black',
                            'target-arrow-color': 'black',
                            'source-arrow-color': 'black'
                        })
                        .selector('.faded')
                        .css({
                            'opacity': 0.65,
                            'text-opacity': 0.65
                        }),
                        ready: function(){
                        window.cy = this;

                        // giddy up...
                        cy.elements().unselectify();

                        // Event listeners
                        // with sample calling to the controller function as passed as an attribute
                        cy.on('tap', 'node', function(e){
                            var evtTarget = e.cyTarget;
                            var nodeId = evtTarget.id();
							var nodeProps = evtTarget.data('properties');
                            scope.cyClick({value:{id:nodeId,props:nodeProps}});
                        });

                        cy.on('tap', 'edge', function(e){
                        	console.debug("tapping on edge");
                            var evtTarget = e.cyTarget;
                            var edgeId = evtTarget.id();
							var edgeProps = evtTarget.data('properties');
                            scope.cyEdgeclick({value:{id:edgeId,props:edgeProps}});
                        });

                        // load the objects array
                        // use cy.add() / cy.remove() with passed data to add or remove nodes and edges without rebuilding the graph
                        // sample use can be adding a passed variable which will be broadcast on change
                        console.debug(scope.elements);
                        cy.load(scope.elements);
                        cy.cxtmenu({
        					selector: 'node',

        					commands: [
        						{
        							content: '<span class="fa fa-flash fa-2x"></span>',
        							select: function(ele){
        								console.log( ele.id() );
        								console.log(ele);
        							}
        						},

        						{
        							content: 'Rename',
        							select: function(ele){
        								console.log('Directive rename');
        								console.log( ele.data('name') );
        								console.log( 'id:'+ele.id() );
        								scope.cyRenamenode({value:{id:ele.id()}});
        							},
        						},

        						{
        							content: 'Remove',
        							select: function(ele){
        								console.log('Directive remove');
        								console.log( 'position:'+ele.position() );
        								console.log( 'id:'+ele.id() );
        								console.log(scope.elements);
        								scope.cyRemove({value:{id:ele.id()}});
        								console.log(scope.elements);
        							}
        						}
        					]
        				});
                    }
                });

            }; // end doCy()

            // When the app object changed = redraw the graph
            // you can use it to pass data to be added or removed from the object without redrawing it
            // using cy.remove() / cy.add()
            $rootScope.$on('appChanged', function(){
                scope.doCy();
            });
        }
    };
});
