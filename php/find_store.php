<?php

	require_once('config.php');
	
	$filter = [];
	$options = [
		'sort' => ['id' => 1],
	];
	$query = new MongoDB\Driver\Query($filter, $options);
	$cursor = $manager->executeQuery('sample.store', $query);

	$data = array();

	foreach ($cursor as $row) {
		$result = array();
		
		$result ['id'] = $row->id;
		$result ['name'] = $row->name;
		$result ['type'] = $row->type;
		$result ['address'] = $row->address;
		$result ['image'] = $row->image;
		
		array_push($data, $result);           
	}

	header('Content-Type: application/json; charset=utf8');
	$json = json_encode(array("store"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	echo $json;
	
?>