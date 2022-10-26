import React from 'react'

function Profile() {

	const [username, setUserName] = React.useState('')
	const [email, setEmail] = React.useState('')
	const [photoURL, setphotoURL] = React.useState('')

	React.useEffect(() => {
		setUserName(localStorage.getItem('username'))
		setEmail(localStorage.getItem('useremail'))
		setphotoURL(localStorage.getItem('userphoto'))
	}, [])


	return (
		<div className='container mt-3'>
			<div className="container">
				<div className="row">
					
						<div className="col-4 text-end">
							<img src={photoURL} alt="profile" />
						</div>
						<div className="col-4 mt-3">
							Name  : {username} <br />
							Email : {email}
						</div>
				</div>
			</div>
		</div>
	)
}

export default Profile
