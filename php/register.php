<?php

	error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    require_once('config.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {   
		$id=$_POST['id'];
		$password=$_POST['password'];

        if(empty($id)){
            $errMSG = "ID를 입력하세요.";
        }
        else if(empty($password)){
            $errMSG = "비밀번호를 입력하세요.";
        }
		
		if(!isset($errMSG)){
			$insRec       = new MongoDB\Driver\BulkWrite;
            $insRec->insert(['id'=>$id, 'password'=>$password, 'order'=>[]]);

            $writeConcern = new MongoDB\Driver\WriteConcern(MongoDB\Driver\WriteConcern::MAJORITY, 1000);
         
            $result       = $manager->executeBulkWrite('sample.user', $insRec, $writeConcern);
			if($result->getInsertedCount()){
				$successMSG = "새로운 회원정보를 추가했습니다.";
			}
			else{
				$errMSG = "회원정보 추가 에러";
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
                Id: <input type = "text" name = "id" />
                Password: <input type = "text" name = "password" />
                <input type = "submit" name = "submit" />
            </form>
       
       </body>
    </html>

<?php 
    }
?>