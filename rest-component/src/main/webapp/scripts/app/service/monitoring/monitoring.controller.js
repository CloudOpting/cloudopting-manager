'use strict';

angular.module('cloudoptingApp')
    .controller('MonitoringController', function (SERVICE, $scope, $state, $log, localStorageService, InstanceService, MonitoringService, $timeout) {

        //Add the dives to the page dynamically.
        $scope.graphsList = [];

        var callback_line = function(graph) {
            $scope.graphsList.push({
                title: "My First Char Line",
                chartId: "chart_line"
            });
            $timeout(function() {
                lineChart(graph, "chart_line");
            }, 1000);
        };
        MonitoringService.findObject(1, 1, callback_line);

        var callback_bar = function(graph) {
            $scope.graphsList.push({
                title: "My First Char Bar",
                chartId: "chart_bar"
            });
            $timeout(function() {
                barChart(graph, "chart_bar");
            }, 1000);
        };
        MonitoringService.findObject(1, 1, callback_bar);

        var elasticcallback_line = function(graph) {
            $scope.graphsList.push({
                title: "Elastic Chart Line",
                chartId: "elasticchart_line"
            });
            $timeout(function() {
                lineChart(graph, "elasticchart_line");
            }, 1000);
        };
        
        MonitoringService.findOneDataById(1,elasticcallback_line);

        var elasticcallback_bar = function(graph) {
            $scope.graphsList.push({
                title: "Elastic Chart Bar",
                chartId: "elasticchart_bar"
            });
            $timeout(function() {
                barChart(graph, "elasticchart_bar");
            }, 1000);
        };
        MonitoringService.findOneDataById(1,elasticcallback_bar);

        var newelasticcallback_line = function(graph) {
            $scope.graphsList.push({
                title: "New Elastic Chart Line",
                chartId: "newelasticchart_line"
            });
            $timeout(function() {
                lineChart(graph, "newelasticchart_line");
            }, 1000);
        };

        MonitoringService.findByCustomizationId(1,newelasticcallback_line);

        var newelasticcallback_bar = function(graph) {
            $scope.graphsList.push({
                title: "New Elastic Chart Bar",
                chartId: "newelasticchart_bar"
            });
            $timeout(function() {
                barChart(graph, "newelasticchart_bar");
            }, 1000);
        };
        MonitoringService.findByCustomizationId(1,newelasticcallback_bar);


        var lineChart = function(graph, chart_name) {
            new Morris.Line({
                // ID of the element in which to draw the chart.
                element: chart_name,
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
                xkey: graph.xkey, /*'time',*/
                // A list of names of data record attributes that contain y-values.
                ykeys: graph.ykeys, /*['disk', 'cpu', 'ram'],*/
                // Labels for the ykeys -- will be displayed when you hover over the
                // chart.
                labels: graph.labels, /*['Disk', 'CPU', 'RAM'],*/
                lineColors: graph.lineColors/*['green', 'blue', 'orange']*/
            });
        };

        var barChart = function(graph, chart_name) {
            new Morris.Bar({
                // ID of the element in which to draw the chart.
                element: chart_name,
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
                xkey: graph.xkey, /*'time',*/
                // A list of names of data record attributes that contain y-values.
                ykeys: graph.ykeys, /*['disk', 'cpu', 'ram'],*/
                // Labels for the ykeys -- will be displayed when you hover over the
                // chart.
                labels: graph.labels, /*['Disk', 'CPU', 'RAM'],*/
                barColors: graph.lineColors/*['green', 'blue', 'orange']*/
            });
        };
    }
);
