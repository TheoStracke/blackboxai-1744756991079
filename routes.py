from flask import jsonify, request
from app import app, db
from models import User, Pizza, Extra, Combo, Order
import json

# User Routes
@app.route('/api/users', methods=['POST'])
def create_user():
    data = request.get_json()
    
    # Validate required fields
    required_fields = ['name', 'email', 'phone', 'cep']
    for field in required_fields:
        if field not in data:
            return jsonify({'error': f'Missing required field: {field}'}), 400
    
    # Check if user already exists
    if User.query.filter_by(email=data['email']).first():
        return jsonify({'error': 'Email already registered'}), 400
    
    # Create new user
    new_user = User(
        name=data['name'],
        email=data['email'],
        phone=data['phone'],
        cep=data['cep']
    )
    
    db.session.add(new_user)
    db.session.commit()
    
    return jsonify({'message': 'User created successfully', 'id': new_user.id}), 201

# Pizza Routes
@app.route('/api/pizzas', methods=['GET'])
def get_pizzas():
    pizzas = Pizza.query.all()
    return jsonify([{
        'id': p.id,
        'name': p.name,
        'description': p.description,
        'price': p.price,
        'image_url': p.image_url,
        'is_featured': p.is_featured
    } for p in pizzas])

@app.route('/api/pizzas/featured', methods=['GET'])
def get_featured_pizzas():
    featured = Pizza.query.filter_by(is_featured=True).all()
    return jsonify([{
        'id': p.id,
        'name': p.name,
        'description': p.description,
        'price': p.price,
        'image_url': p.image_url
    } for p in featured])

# Extras Routes (Bordas e Bebidas)
@app.route('/api/extras', methods=['GET'])
def get_extras():
    extras = Extra.query.all()
    return jsonify([{
        'id': e.id,
        'name': e.name,
        'price': e.price,
        'type': e.type
    } for e in extras])

@app.route('/api/extras/<type>', methods=['GET'])
def get_extras_by_type(type):
    if type not in ['borda', 'bebida']:
        return jsonify({'error': 'Invalid type'}), 400
        
    extras = Extra.query.filter_by(type=type).all()
    return jsonify([{
        'id': e.id,
        'name': e.name,
        'price': e.price,
        'type': e.type
    } for e in extras])

# Combo Routes
@app.route('/api/combos', methods=['GET'])
def get_combos():
    combos = Combo.query.all()
    return jsonify([{
        'id': c.id,
        'name': c.name,
        'description': c.description,
        'price': c.price,
        'items': json.loads(c.items),
        'image_url': c.image_url
    } for c in combos])

# Order Routes
@app.route('/api/orders', methods=['POST'])
def create_order():
    data = request.get_json()
    
    # Validate required fields
    required_fields = ['user_id', 'items', 'total_price']
    for field in required_fields:
        if field not in data:
            return jsonify({'error': f'Missing required field: {field}'}), 400
    
    # Check if user exists
    user = User.query.get(data['user_id'])
    if not user:
        return jsonify({'error': 'User not found'}), 404
    
    # Create new order
    new_order = Order(
        user_id=data['user_id'],
        items=json.dumps(data['items']),
        total_price=data['total_price']
    )
    
    db.session.add(new_order)
    db.session.commit()
    
    return jsonify({
        'message': 'Order created successfully',
        'order_id': new_order.id
    }), 201

@app.route('/api/orders/<int:user_id>', methods=['GET'])
def get_user_orders(user_id):
    # Check if user exists
    user = User.query.get(user_id)
    if not user:
        return jsonify({'error': 'User not found'}), 404
        
    orders = Order.query.filter_by(user_id=user_id).all()
    return jsonify([{
        'id': o.id,
        'total_price': o.total_price,
        'status': o.status,
        'created_at': o.created_at.isoformat(),
        'items': json.loads(o.items)
    } for o in orders])

# Error handlers
@app.errorhandler(404)
def not_found(error):
    return jsonify({'error': 'Not found'}), 404

@app.errorhandler(500)
def server_error(error):
    return jsonify({'error': 'Internal server error'}), 500
