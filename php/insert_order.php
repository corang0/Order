<?php

	error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    require_once('config.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {   
		$id=$_POST['id'];
		$name=$_POST['name'];
		$store=$_POST['store'];
        $quantity=$_POST['quantity'];

        if(empty($id)){
            $errMSG = "아이디를 입력하세요.";
        }
        else if(empty($name)){
            $errMSG = "상품명을 입력하세요.";
        }
		else if(empty($store)){
			$errMSG = "가게명을 입력하세요.";
		}
		else if(empty($quantity)){
			$errMSG = "수량을 입력하세요.";
		}
		
		if(!isset($errMSG)){
			$filter = ['id' => $id];
			$options = [
				'sort' => ['_id' => -1],
			];
			$query = new MongoDB\Driver\Query($filter, $options);
			$cursor = $manager->executeQuery('sample.store', $query);
			
			foreach ($cursor as $row) {
				$owner = $row->owner;    
			}
			
			$filter = ['id' => $owner];
			$options = [
				'sort' => ['_id' => -1],
			];
			$query = new MongoDB\Driver\Query($filter, $options);
			$cursor = $manager->executeQuery('sample.user', $query);
			
			$data = array();

			foreach ($cursor as $row) {
				$data = $row->order;    
			}
			
			$oid = (string)(count($data) + 1);
		
			$insRec       = new MongoDB\Driver\BulkWrite;
            $insRec->update(['id' => $owner],['$push' =>['order' =>['id'=>$oid, 'store'=>$store, 'name'=>$name, 'quantity'=>$quantity, 'image'=>"0"]]], ['multi' => false, 'upsert' => false]);

            $writeConcern = new MongoDB\Driver\WriteConcern(MongoDB\Driver\WriteConcern::MAJORITY, 1000);
         
            $result       = $manager->executeBulkWrite('sample.user', $insRec, $writeConcern);
			if($result->getModifiedCount()){
				$successMSG = "새로운 데이터를 추가했습니다.";
			}
			else{
				$errMSG = "데이터 추가 에러";
			}
        }
    }
	
?>

<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
                Name: <input type = "text" name = "name" />
                Store: <input type = "text" name = "store" />
				Quantity: <input type = "text" name = "quantity" />
                <input type = "submit" name = "submit" />
            </form>
       
       </body>
    </html>

<?php 
    }
?>