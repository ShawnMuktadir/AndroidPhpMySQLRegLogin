<?php
	/**
	 * 
	 */
	class DbOperations 
	{
		private $con;
		
		function __construct()
		{
			require_once dirname(__FILE__).'/DbConnect.php';

			$db = new DbConnect();

			$this->con = $db->connect();
		}

		/* Create User*/

		public function createUser($email,$username,$phonenumber,$pass,$bloodgroup,$address,$category){
			
			if($this->isUserExist($username,$email,$category)){
                return 0; //0 is for user exist
            }


			else {
				$password = md5($pass);
			    $stmt = $this->con->prepare("INSERT INTO `registration` (`id`, `email`, `username`, `phonenumber`, `password`, `bloodgroup`, `address`, `category`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?);");
			    $stmt->bind_param("ssissss",$email,$username,$phonenumber,$password,$bloodgroup,$address,$category);

			    if ($stmt->execute()) {
				return 1; //1 is for new user registered successfully
			    } else {
				return 2; //2 means some error occured
					}

			}
		}

		private function isUserExist($username, $email, $category){
            $stmt = $this->con->prepare("SELECT id FROM registration WHERE username = ? OR email = ? OR category = ? ");
            $stmt->bind_param("sss", $username, $email, $category);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0; //it will return either true or false means user exist
        }

        public function userLogin($username,$pass,$category){
            $password = md5($pass);
            $stmt = $this->con->prepare("SELECT id FROM registration WHERE username = ? AND password = ? AND category = ?");
            $stmt->bind_param("sss",$username,$password,$category);
            $stmt->execute();
            $stmt->store_result();
            return $stmt->num_rows > 0; //means user exists,so, log in
            
        }

        public function getUserByUsername($username,$category){
            $stmt = $this->con->prepare("SELECT * FROM registration WHERE username = ? AND category = ? ");
            $stmt->bind_param("ss",$username,$category);
            $stmt->execute();
            return $stmt->get_result()->fetch_assoc(); 
        }
	}

?>