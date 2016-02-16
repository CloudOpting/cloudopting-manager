'use strict';

angular.module('cloudoptingApp')
    .controller('MonitoringController', function ($scope, localStorageService, SERVICE, $state, $log, MonitoringService, $timeout, Principal) {

        var instance = localStorageService.get(SERVICE.STORAGE.CURRENT_INSTANCE);

        //Add the dives to the page dynamically.
        $scope.graphsList = [];
        $scope.zabbixgraph = {
        		data: null,
        		labels: null,
                xkey: 'clock',
                ykeys: 'value',
                lineColors: ['green'],
                dataFormat: function(x){return new Date(x*1000).toString();}
                };
        
        $scope.zabbixdata = {
        		hostSelect: null,
        		hostOptions: null ,
        		itemsSelect: null,
        		itemsOptions: null,
        		chartData: null,
        };

        
        
        var zabhostcallback = function(data, status, headers, config) {
        	checkStatusCallback(data, status, headers, config);
        	if(data){
        		$scope.zabbixdata.hostOptions = angular.fromJson(data);
        	}
        };
        
        var zabitemscallback = function(data, status, headers, config) {
        	checkStatusCallback(data, status, headers, config);
        	if(data){
        		$scope.zabbixdata.itemsOptions = angular.fromJson(data);
        	}
        };
        
        var zabhystcallback = function(data, status, headers, config) {
        	checkStatusCallback(data, status, headers, config);
        	var propsToConvert = {
        		clock: 1,
        		value:1
        	};
        	if(data){
//        		$scope.zabbixdata.chartData = angular.fromJson(data);
//        		console.log($scope.zabbixdata.chartData);
/*        		$scope.zabbixgraph.data =  JSON.parse(data, function(key, value){
        			if(propsToConvert.hasOwnProperty(key)){
        				return parseInt(value, 10);
        			}
        			return value;
        		});
        		*/
            	$scope.zabbixgraph.data =  angular.fromJson(data);
            	$scope.zabbixgraph.labels = [$scope.zabbixdata.itemsSelect.name];
            	console.log($scope.zabbixgraph);
                $timeout(function () {
                	lineChartZabb($scope.zabbixgraph, $scope.zabbixdata.itemsSelect.itemid);
                }, 1000);
        	}
        };

        MonitoringService.findAllZabbixHosts(instance.id, zabhostcallback);
        
        $scope.updateItems = function(id){
        	console.log(id);
        	MonitoringService.findAllZabbixItems(instance.id, id, zabitemscallback);
        };
        
        $scope.updateChart = function(id){
        	console.log(id);
        	MonitoringService.findZabbixHistory(instance.id, $scope.zabbixdata.hostSelect.hostid, id, zabhystcallback);
        };
        
        var callback = function(data, status, headers, config) {
            checkStatusCallback(data, status, headers, config);
            if(data){
                var graph = data;
                $scope.graphsList.push({
                    title: "findObject Char Line (ID:1)",
                    chartId: "chart_line"
                });
                $scope.graphsList.push({
                    title: "findObject Char Bar (ID:1)",
                    chartId: "chart_bar"
                });
                $timeout(function() {
                    lineChart(graph, "chart_line");
                    barChart(graph, "chart_bar");
                }, 1000);
            }
        };
        MonitoringService.findObject(1, 1, callback);

        var elasticcallback =  function(data, status, headers, config) {
            checkStatusCallback(data, status, headers, config);
            if(data) {
                var graph = data;
                $scope.graphsList.push({
                    title: "Elastic Chart Line (dynamic)",
                    chartId: "elasticchart_line"
                });
                $scope.graphsList.push({
                    title: "Elastic Chart Bar (dynamic)",
                    chartId: "elasticchart_bar"
                });
                $timeout(function () {
                    lineChart(graph, "elasticchart_line");
                    barChart(graph, "elasticchart_bar");
                }, 1000);
            }
        };
        
        MonitoringService.findOneDataById(instance.id, elasticcallback);

        var newelasticcallback =  function(data, status, headers, config) {
            checkStatusCallback(data, status, headers, config);
            if(data) {
                var graph = data;
                $scope.graphsList.push({
                    title: "New Elastic Chart Line (ID:1)",
                    chartId: "newelasticchart_line"
                });
                $scope.graphsList.push({
                    title: "New Elastic Chart Bar (ID:1)",
                    chartId: "newelasticchart_bar"
                });
                $timeout(function () {
                    lineChart(graph, "newelasticchart_line");
                    barChart(graph, "newelasticchart_bar");
                }, 1000);
            }
        };
        MonitoringService.findByCustomizationId(1,newelasticcallback);



        var lineChartZabb = function(graph, chart_name) {
            new Morris.Line({
                // ID of the element in which to draw the chart.
                element: chart_name,
                // Chart data records -- each entry in this array corresponds to a point on
                // the chart.
                data: graph.data,

                xkey: 'clock', 
                // A list of names of data record attributes that contain y-values.
                ykeys: ['value'], /*['disk', 'cpu', 'ram'],*/
                // Labels for the ykeys -- will be displayed when you hover over the
                // chart.
                labels: graph.labels, /*['Disk', 'CPU', 'RAM'],*/
                lineColors: graph.lineColors,/*['green', 'blue', 'orange']*/
                dateFormat: function(x){return new Date(x).toString();},
            });
        };

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
                lineColors: graph.lineColors,/*['green', 'blue', 'orange']*/
                
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

        $scope.errorMessage = null;
        $scope.infoMessage = null;

        //function to check possible outputs for the end user information.
        var checkStatusCallback = function(data, status, headers, config){
            if(status==401) {
                //Unauthorised. Check if signed in.
                if(Principal.isAuthenticated()){
                    $scope.errorMessage = "You have no permissions to do so. Ask for more permissions to the administrator";
                } else {
                    $scope.errorMessage = "Your session has ended. Sign in again. Redirecting to login...";
                    $timeout(function() {
                        $state.go('login');
                    }, 3000);
                }
            }else if(status!=200 && status!=201) {
                //Show message
                $scope.errorMessage = "An error occurred. Wait a moment and try again, if problem persists contact the administrator";

            } else {
                $log.info("Successful.");
            }
        };
    }
);
