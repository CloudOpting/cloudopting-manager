'use strict';

angular.module('cloudoptingApp')
    .controller('MonitoringController', function (SERVICE, $scope, $state, $log, localStorageService, InstanceService, MonitoringService) {

        //We should get the information of the current instance.

        //var graph = null;

        var callback_line = function(graph) {
            console.log(graph);
            new Morris.Line({
                // ID of the element in which to draw the chart.
                element: 'chart_line',
                // Chart data records -- each entry in this array corresponds to a point on
                // the chart.
                data: graph.data,

                /*[
                 { time: '2001', disk: 20, cpu: 12, ram: 15 },
                 { time: '2002', disk: 10, cpu: 5, ram: 13 },
                 { time: '2003', disk: 5, cpu: 20, ram: 19 },
                 { time: '2004', disk: 5, cpu: 3, ram: 0 },
                 { time: '2005', disk: 20, cpu: 10, ram: 5 }
                 ],*/
                // The name of the data record attribute that contains x-values.
                xkey: graph.xkey,/*'time',*/
                // A list of names of data record attributes that contain y-values.
                ykeys: graph.ykeys,/*['disk', 'cpu', 'ram'],*/
                // Labels for the ykeys -- will be displayed when you hover over the
                // chart.
                labels: graph.labels,/*['Disk', 'CPU', 'RAM'],*/
                lineColors: graph.lineColors/*['green', 'blue', 'orange']*/
            });
        };
        MonitoringService.findObject(1, 1, callback_line);


        //var graph_bar = null;

        var callback_bar = function(graph) {
            console.log(graph);
            new Morris.Bar({
                // ID of the element in which to draw the chart.
                element: 'chart_bar',
                // Chart data records -- each entry in this array corresponds to a point on
                // the chart.
                data: graph.data,

                /*[
                 { time: '2001', disk: 20, cpu: 12, ram: 15 },
                 { time: '2002', disk: 10, cpu: 5, ram: 13 },
                 { time: '2003', disk: 5, cpu: 20, ram: 19 },
                 { time: '2004', disk: 5, cpu: 3, ram: 0 },
                 { time: '2005', disk: 20, cpu: 10, ram: 5 }
                 ],*/
                // The name of the data record attribute that contains x-values.
                xkey: graph.xkey,/*'time',*/
                // A list of names of data record attributes that contain y-values.
                ykeys: graph.ykeys,/*['disk', 'cpu', 'ram'],*/
                // Labels for the ykeys -- will be displayed when you hover over the
                // chart.
                labels: graph.labels,/*['Disk', 'CPU', 'RAM'],*/
                lineColors: graph.lineColors/*['green', 'blue', 'orange']*/
            });
        };
        MonitoringService.findObject(1, 1, callback_bar);

        //var elasticgraph = null;
        var elasticcallback_line = function(graph) {
            console.log(graph);
            new Morris.Line({
                // ID of the element in which to draw the chart.
                element: 'elasticchart_line',
                // Chart data records -- each entry in this array corresponds to a point on
                // the chart.
                data: graph.data,

                // The name of the data record attribute that contains x-values.
                xkey: graph.xkey,/*'time',*/
                // A list of names of data record attributes that contain y-values.
                ykeys: graph.ykeys,/*['disk', 'cpu', 'ram'],*/
                // Labels for the ykeys -- will be displayed when you hover over the
                // chart.
                labels: graph.labels,/*['Disk', 'CPU', 'RAM'],*/
                lineColors: graph.lineColors/*['green', 'blue', 'orange']*/
            });
        };
        
        MonitoringService.findOneDataById(1,elasticcallback_line);

        //var elasticgraph_B = null;
        var elasticcallback_bar = function(graph) {
            console.log(graph);
            new Morris.Bar({
                // ID of the element in which to draw the chart.
                element: 'elasticchart_bar',
                // Chart data records -- each entry in this array corresponds to a point on
                // the chart.
                data: graph.data,

                // The name of the data record attribute that contains x-values.
                xkey: graph.xkey,/*'time',*/
                // A list of names of data record attributes that contain y-values.
                ykeys: graph.ykeys,/*['disk', 'cpu', 'ram'],*/
                // Labels for the ykeys -- will be displayed when you hover over the
                // chart.
                labels: graph.labels,/*['Disk', 'CPU', 'RAM'],*/
                //lineColors: graph.lineColors/*['green', 'blue', 'orange']*/
            });
        };

        MonitoringService.findOneDataById(1,elasticcallback_bar);

    }
);
